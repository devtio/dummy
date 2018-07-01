package io.devtio.dummy.service.setups.model;

import java.util.LinkedList;
import java.util.List;

public class NamespaceModel {
    private String name;
    private List<AppModel> apps;

    public NamespaceModel(String name) {
        this.name = name;
        this.apps = new LinkedList<>();
    }

    public void addApp(AppModel appModel) {
        this.apps.add(appModel);
    }

    public String getName() {
        return name;
    }

    public List<AppModel> getApps() {
        return apps;
    }

}
