package message;

public abstract class Request {
    private String user;
    private long requestTime;

    Request(String user, long requestTime) {
        this.user = user;
        this.requestTime = requestTime;
    }

    public String getUser() {
        return user;
    }

    public long getRequestTime() {
        return requestTime;
    }
}
