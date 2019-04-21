package message;

public abstract class Response {
    private String user;
    private String errMsg;
    private long responseTime;

    Response(String user, long responseTime) {
        this.user = user;
        this.errMsg = "";
        this.responseTime = responseTime;
    }

    Response(String user, String errMsg, long responseTime) {
        this.user = user;
        this.errMsg = errMsg;
        this.responseTime = responseTime;
    }

    public String getUser() {
        return user;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void print() {
        System.out.println("Response---");
        System.out.println("user:" + user);
        System.out.println("errMsg:" + errMsg);
        System.out.println("responseTime:" + responseTime);
    }
}
