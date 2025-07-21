package server_instance.factory;

import server_instance.*;
import server_instance.ServerInstanceManagement;

/**
 * A concrete factory that creates different types of server instances based on a given application name.
 */
public class ServerInstanceFactory {

    private final String autName;

    /**
     * Constructs a factory for a specific application type.
     * @param autName The name of the Application Under Test (e.g., "spring_petclinic_with_no_coverage").
     */
    public ServerInstanceFactory(String autName) {
        if (autName == null || autName.trim().isEmpty()) {
            throw new IllegalArgumentException("Application name (autName) cannot be null or empty.");
        }
        this.autName = autName;
    }

    public ServerInstanceManagement create(final int port) {
        switch (this.autName) {
            case "timeoff_management_with_coverage":
                return new TimeOffManagementServer(this.autName, port);
            case "nodebb_with_coverage":
                return new NodeBBServer(this.autName, port);
            case "keystonejs_with_coverage":
                return new KeystoneJSServer(this.autName, port);
            case "wagtails":
                return new WagtailsServer(this.autName, port);
            case "django_blog_with_no_coverage":
                return new DjangoBlogServer(this.autName, port);
            case "spring_petclinic_with_no_coverage":
                return new SpringPetclinicServer(this.autName, port);
            case "kimai":
                return new KimaiServer(this.autName, port);
            case "astuto":
                return new AstutoServer(this.autName, port);
            case "svelte_commerce":
                return new SvelteCommerceServer(this.autName, port);
            case "oscar":
                return new OscarServer(this.autName, port);
        }
        throw new RuntimeException("AUT not fount when create server instance.");
    }
}