package server_instance.proxy;

import java.util.Optional;

public abstract class ProxyManager {

    /**
     * Updates the proxy to route traffic to a single backend endpoint.
     * An empty Optional means the proxy will have no active backend.
     * @param backendEndpoint An Optional containing the single endpoint string.
     */
    public abstract void updateBackend(Optional<String> backendEndpoint);

    /**
     * Provides a default shutdown method.
     */
    public void shutdown() {
        // Default implementation does nothing.
    }
}
