package server_instance.pool;

import server_instance.ServerInstanceManagement;
import server_instance.factory.ServerInstanceFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * A pure resource pool. Its sole responsibility is to maintain a queue of READY server instances.
 * It does not track the IN_USE state, which is handled by the consumer.
 */
public class ServerInstancePoolManager {
    final int MAX_RETRIES = 9;
    final long DELAY_SECONDS = 5;

    private final int poolSize;
    private final String appNamePrefix;
    private final BlockingQueue<Integer> availablePorts;
    private final ServerInstanceFactory instanceFactory;
    private final List<ManagedServerInstance> managedInstances = new ArrayList<>();
    private final BlockingQueue<ManagedServerInstance> readyInstancesQueue;
    private final ExecutorService instanceTaskExecutor = Executors.newCachedThreadPool();

    public ServerInstancePoolManager(String appNamePrefix, int startPort, int endPort, 
                                     ServerInstanceFactory instanceFactory) {
        this.appNamePrefix = appNamePrefix;
        this.poolSize = endPort - startPort + 1;
        this.instanceFactory = instanceFactory;
        
        // Initialize the available ports queue.
        this.availablePorts = new ArrayBlockingQueue<Integer>(poolSize);
        IntStream.rangeClosed(startPort, endPort).forEach(this.availablePorts::add);

        this.readyInstancesQueue = new ArrayBlockingQueue<ManagedServerInstance>(poolSize);
    }

    public void initialize() {
        System.out.println("[PoolManager] Initializing pool with " + poolSize + " instances. This will take some time...");
        List<Future<?>> creationFutures = new ArrayList<Future<?>>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            ManagedServerInstance managedInstance = createManagedInstanceObject();
            if (managedInstance == null) {
                System.err.println("[PoolManager] Initialization failed: No available ports to create instance.");
                continue;
            }
            managedInstances.add(managedInstance);
            
            Future<?> future = instanceTaskExecutor.submit(() -> createAndReadyInstance(managedInstance));
            creationFutures.add(future);
        }
        for (Future<?> future : creationFutures) {
            try {
                future.get();
            } catch (Exception e) {
                System.err.println("[PoolManager] A pool initialization task was interrupted or failed after all retries.");
            }
        }
        System.out.println("[PoolManager] Pool initialization complete. " + readyInstancesQueue.size() + " instances are ready.");
    }

    public ManagedServerInstance acquireInstance() {
        System.out.println("[PoolManager] A consumer is waiting to acquire a ready instance...");
        try {
            ManagedServerInstance managedInstance = readyInstancesQueue.take();
            System.out.println("[PoolManager] Provided instance " + managedInstance.getInstance().getAppName() + " to consumer.");
            return managedInstance;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while waiting for a server instance.", e);
        }
    }

    public void recycleInstanceAsync(ManagedServerInstance managedInstance) {
        if (managedInstance == null)
            return;
        System.out.println("[PoolManager] Received instance " + managedInstance.getInstance().getAppName() + " for recycling.");
        managedInstance.setState(ServerInstanceState.CREATING);

        instanceTaskExecutor.submit(() -> {
            final ServerInstanceManagement serverInstance = managedInstance.getInstance();
            final int portToRecycle = serverInstance.getServerPort();

            serverInstance.closeServerInstance();
            serverInstance.createServerInstance();

            System.out.println("[PoolManager] Instance " + managedInstance.getInstance().getAppName() + " is READY.");
            managedInstance.setState(ServerInstanceState.READY);
            try {
                readyInstancesQueue.put(managedInstance);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // just set the state to FAILED, and then give up.
                managedInstance.setState(ServerInstanceState.FAILED);
            }

            // The self-healing mechanism is triggered after recycling.
            triggerRepairForOneFailedInstance();
        });
    }

    private void createAndReadyInstance(ManagedServerInstance managedInstance) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                // If this is a repair task, the state is already REPAIR_PENDING.
                if (managedInstance.getState() != ServerInstanceState.REPAIR_PENDING) {
                    managedInstance.setState(ServerInstanceState.CREATING);
                }
                System.out.println("[PoolManager] Attempt " + attempt + ": Creating container for " + managedInstance.getInstance().getAppName());
                
                managedInstance.getInstance().createServerInstance();
                
                managedInstance.setState(ServerInstanceState.READY);
                readyInstancesQueue.put(managedInstance);
                System.out.println("[PoolManager] Instance " + managedInstance.getInstance().getAppName() + " is now READY.");
                return;

            } catch (Exception e) {
                System.err.println("[PoolManager] Attempt " + attempt + " failed for instance " + managedInstance.getInstance().getAppName() + ": " + e.getMessage());
                if (attempt < MAX_RETRIES) {
                    try {
                        System.err.println("[PoolManager] Retrying in " + DELAY_SECONDS + " seconds...");
                        TimeUnit.SECONDS.sleep(DELAY_SECONDS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break; 
                    }
                }
            }
        }

        managedInstance.setState(ServerInstanceState.FAILED);
        System.err.println("[PoolManager] All " + MAX_RETRIES + " retries failed for instance " 
                           + managedInstance.getInstance().getAppName() + ". Marked as FAILED.");
    }

    private void triggerRepairForOneFailedInstance() {
        for (ManagedServerInstance instanceToRepair : managedInstances) {
            // Use synchronization on the specific instance object as a lock.
            synchronized (instanceToRepair) {
                if (instanceToRepair.getState() == ServerInstanceState.FAILED) {
                    // Atomically claim this instance for repair.
                    instanceToRepair.setState(ServerInstanceState.REPAIR_PENDING);
                    System.out.println("[PoolManager] Event-driven self-healing: Claimed FAILED instance " 
                                       + instanceToRepair.getInstance().getAppName() + " for repair.");
                    // Submit the repair task.
                    instanceTaskExecutor.submit(() -> createAndReadyInstance(instanceToRepair));
                    // Only repair one failed instance per event.
                    return;
                }
            }
        }
    }

    private ManagedServerInstance createManagedInstanceObject() {
        Integer port;
        try {
            // Take a port from the pool. This will block if no ports are available.
            port = availablePorts.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        // Use the provided factory to create the specific server instance.
        ServerInstanceManagement newServerInstance = this.instanceFactory.create(port);
        return new ManagedServerInstance(newServerInstance);
    }
    
    public void shutdown() {
        System.out.println("[PoolManager] Shutting down pool...");
        instanceTaskExecutor.shutdownNow();
        managedInstances.forEach(p -> {
            try {
                p.getInstance().closeServerInstance();
            } catch (Exception e) {
                // Ignore errors during shutdown
            }
        });
        System.out.println("[PoolManager] Pool shutdown complete.");
    }
}
