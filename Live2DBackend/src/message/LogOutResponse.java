package message;

public class LogOutResponse extends Response {
    public enum LogOutResponseType {PASSED, ALREADY_LOG_OUT, ERR}

    private LogOutResponseType responseType;

    public LogOutResponse(LogOutResponseType responseType, String user, long responseTime) {
        super(user, responseTime);
        this.responseType = responseType;
    }

    public LogOutResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = LogOutResponseType.ERR;
    }

    public LogOutResponseType getResponseType() {
        return responseType;
    }
}
