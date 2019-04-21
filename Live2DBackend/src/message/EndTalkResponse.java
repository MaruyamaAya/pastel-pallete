package message;

public class EndTalkResponse extends Response {
    public enum EndTalkResponseType {PASSED, ALREADY_ENDED, ERR}

    private EndTalkResponseType responseType;

    public EndTalkResponse(EndTalkResponseType responseType, String user, long responseTime) {
        super(user, responseTime);
        this.responseType = responseType;
    }

    public EndTalkResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = EndTalkResponseType.ERR;
    }

    public EndTalkResponseType getResponseType() {
        return responseType;
    }
}
