package be.howest.ti.mars.logic.domain;

public class NotificationData {

    private final String endpoint;
    private final String userkey;
    private final String auth;

    public NotificationData(String endpoint, String userkey, String auth) {
        this.endpoint = endpoint;
        this.userkey = userkey;
        this.auth = auth;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getUserkey() {
        return userkey;
    }

    public String getAuth() {
        return auth;
    }
}
