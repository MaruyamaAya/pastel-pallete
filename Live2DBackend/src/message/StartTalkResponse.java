package message;

public class StartTalkResponse extends Response {
    public enum StartTalkResponseType {PVP_CONNECTED, PVP_WAITING, PVE_CONNECTED, ALREADY_CONNECTED, ERR}

    private StartTalkResponseType responseType;

    public StartTalkResponse(StartTalkResponseType responseType, String user, long responseTime) {
        super(user, responseTime);
        this.responseType = responseType;
    }

    public StartTalkResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = StartTalkResponseType.ERR;
    }

    public StartTalkResponseType getResponseType() {
        return responseType;
    }
}
