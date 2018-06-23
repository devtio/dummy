package io.devtio.dummy.service.setups.model;

import java.util.List;

public class AppModel {
    private String name;
    private List<String> versions;

    public AppModel(String name, List<String> versions) {
        this.name = name;
        this.versions = versions;
    }

    public String getName() {
        return name;
    }

    public List<String> getVersions() {
        return versions;
    }
}
