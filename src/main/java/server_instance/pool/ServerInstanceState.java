package server_instance.pool;

public enum ServerInstanceState {
    CREATING,        // The instance is being created or recreated.
    READY,           // The instance is running and available in the pool.
    IN_USE,          // The instance is the single, active target of the proxy.
    FAILED,          // The instance failed to create after all retries.
    REPAIR_PENDING   // The instance has been claimed by a task for repair.
}
