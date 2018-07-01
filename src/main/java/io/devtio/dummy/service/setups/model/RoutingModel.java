package io.devtio.dummy.service.setups.model;

import java.util.Map;

public class RoutingModel {
    private Boolean externalService;

    public RoutingModel(Map<String, Object> routingMap) {
        if (routingMap != null) {
            externalService = (Boolean) routingMap.get("externalService");
        }
    }

    public Boolean isExternalService() {
        return externalService == null ? false : true;
    }
}
