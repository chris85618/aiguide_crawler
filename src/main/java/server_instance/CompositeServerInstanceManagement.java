package server_instance;

import server_instance.codeCoverage.CodeCoverage;
import server_instance.factory.ServerInstanceFactory;
import server_instance.pool.ManagedServerInstance;
import server_instance.pool.ServerInstancePoolManager;
import server_instance.pool.ServerInstanceState;
import server_instance.proxy.NginxProxyManager;
import server_instance.proxy.ProxyManager;
import java.util.function.BiFunction;
import java.util.Optional;

public class CompositeServerInstanceManagement extends ServerInstanceManagement {

    private final ProxyManager proxyManager;
    private final ServerInstancePoolManager poolManager;

    private ManagedServerInstance inUseInstance = null;
    private final Object lock = new Object();

    public CompositeServerInstanceManagement(String appName, int server_port, int startPort, int endPort) {
        this(appName, server_port, startPort, endPort,
             new NginxProxyManager(server_port),
             new ServerInstanceFactory(appName));
    }

    public CompositeServerInstanceManagement(String appName, int server_port, int startPort, int endPort,
                                             ProxyManager proxyManager,
                                             ServerInstanceFactory instanceFactory) {
        // The base constructor is called with a placeholder port, as the actual ports are managed by the pool.
        super(appName, server_port);
        this.proxyManager = proxyManager;
        this.poolManager = new ServerInstancePoolManager(appName, startPort, endPort, instanceFactory);
        this.poolManager.initialize();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Acquires an instance from the pool to become the active proxy target,
     * ONLY if there is no currently active instance.
     * If an instance is already active, this method does nothing and returns the existing one.
     * @return The currently active server instance.
     */
    public ServerInstanceManagement acquireAndGetInstance() {
        System.out.println("\n[Composite] Request to acquire and get an instance...");

        synchronized (lock) {
            // If a slot is already filled, do not acquire a new one.
            if (this.inUseInstance != null) {
                System.out.println("[Composite] An instance is already IN_USE. Returning existing reference: " + this.inUseInstance.getInstance().getAppName());
                this.mergeCoverageToInUseInstance();
                return this.inUseInstance.getInstance();
            }
            // If the slot is empty, acquire a new instance from the pool.
            System.out.println("[Composite] No instance is IN_USE. Acquiring a new one from the pool...");
            this.inUseInstance = poolManager.acquireInstance();
            this.inUseInstance.setState(ServerInstanceState.IN_USE);
        }
        System.out.println("[Composite] Instance " + this.inUseInstance.getInstance().getAppName() + " is now IN_USE.");

        if (this.codeCoverageHelper == null) {
            // Use the first codeCoverageHelp.
            this.codeCoverageHelper = this.inUseInstance.getInstance().getCodeCoverageHelper();
        } else {
            this.mergeCoverageToInUseInstance();
        }

        updateProxyConfiguration();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this.inUseInstance.getInstance();
    }

    @Override
    public void createServerInstance() {
        this.acquireAndGetInstance();
    }

    @Override
    public void closeServerInstance() {
        System.out.println("\n[Composite] Deactivating instance...");
        
        synchronized (lock) {
            if (this.inUseInstance != null) {
                System.out.println("[Composite] Recycling instance " + this.inUseInstance.getInstance().getAppName());
                poolManager.recycleInstanceAsync(this.inUseInstance);
                this.inUseInstance = null;
                updateProxyConfiguration();
            } else {
                System.out.println("[Composite] No active instance to deactivate.");
            }
        }
    }

    /**
     * Performs a hot-swap of the active instance.
     * It deactivates and recycles the current instance, then activates a new one.
     */
    @Override
    public void restartServerInstance() {
        System.out.println("\n[Composite] Performing hot-swap of the active instance...");
        this.closeServerInstance();
        this.createServerInstance();
        System.out.println("[Composite] Hot-swap operation complete.");
    }

    public void shutdown() {
        System.out.println("\n[Composite] Shutting down the system...");
        proxyManager.shutdown();
        poolManager.shutdown();
        System.out.println("[Composite] System shutdown complete.");
    }

    private synchronized void updateProxyConfiguration() {
        System.out.println("[Composite] Updating proxy configuration...");
        Optional<String> endpoint;
        synchronized (lock) {
            if (this.inUseInstance != null) {
                endpoint = Optional.of("localhost:" + this.inUseInstance.getInstance().server_port);
            } else {
                endpoint = Optional.empty();
            }
        }
        proxyManager.updateBackend(endpoint);
    }

    @Override
    public void recordCoverage(){
        final ServerInstanceManagement instance = this.inUseInstance.getInstance();
        instance.recordCoverage();
        final CodeCoverage branchCoverage = instance.getCumulativeBranchCoverage();
        final CodeCoverage statementCoverage = instance.getCumulativeStatementCoverage();
        this.mergeCoverage(statementCoverage, branchCoverage);
    }

    @Override
    public void resetTotalCoverage(){
        this.codeCoverageHelper.resetCumulativeCoverage();
        if (inUseInstance == null) 
            return;
        final ServerInstanceManagement instance = this.inUseInstance.getInstance();
        if (instance == null) 
            return;
        instance.resetTotalCoverage();
    }

    @Override
    public void resetCoverage() {
        this.codeCoverageHelper.resetCoverage();
        if (inUseInstance == null) 
            return;
        final ServerInstanceManagement instance = this.inUseInstance.getInstance();
        if (instance == null) 
            return;
        instance.resetCoverage();
    }

    private void mergeCoverageToInUseInstance(){
        if (inUseInstance == null) 
            return;
        final ServerInstanceManagement instance = this.inUseInstance.getInstance();
        if (instance == null) 
            return;
        final CodeCoverage branchCoverage = this.codeCoverageHelper.getCumulativeBranchCoverage();
        final CodeCoverage statementCoverage = this.codeCoverageHelper.getCumulativeStatementCoverage();
        instance.mergeCoverage(statementCoverage, branchCoverage);
    }
}
