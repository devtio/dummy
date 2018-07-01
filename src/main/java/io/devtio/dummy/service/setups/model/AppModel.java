package io.devtio.dummy.service.setups.model;

import java.util.List;

public class AppModel {
    private String name;
    private String version;
    private RoutingModel routing;

    public AppModel(String name, String version, RoutingModel routing) {
        this.name = name;
        this.version = version;
        this.routing = routing;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public RoutingModel getRouting() {
        return routing;
    }
}
