package io.devtio.dummy.model;

public class Hop {
    private String appId;
    private String appVersion;
    private String error;
    private String thisHop;
    private String nextHop;

    public Hop() {
    }

    public Hop(String appId, String appVersion, String thisHop) {
        this(appId, appVersion, thisHop, null);
    }

    public Hop(String appId, String appVersion, String thisHop, String nextHop) {
        this(appId, appVersion, thisHop, nextHop,null);
    }

    public Hop(String appId, String appVersion, String thisHop, String nextHop, String error) {
        this.appId = appId;
        this.appVersion = appVersion;
        this.error = error;
        this.thisHop = thisHop;
        this.nextHop = nextHop;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getError() {
        return error;
    }

    public String getThisHop() {
        return thisHop;
    }

    public String getNextHop() {
        return nextHop;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }
}
