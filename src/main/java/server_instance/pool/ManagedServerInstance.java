package server_instance.pool;

import server_instance.ServerInstanceManagement;

public class ManagedServerInstance {
    private final ServerInstanceManagement instance;
    private volatile ServerInstanceState state;

    public ManagedServerInstance(ServerInstanceManagement instance) {
        this.instance = instance;
        this.state = ServerInstanceState.CREATING;
    }

    public ServerInstanceManagement getInstance() {
        return instance;
    }

    public ServerInstanceState getState() {
        return state;
    }

    public void setState(ServerInstanceState state) {
        this.state = state;
    }
}
