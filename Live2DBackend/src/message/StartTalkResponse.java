package message;

public class StartTalkResponse extends Response {
    public enum StartTalkResponseType {PVP_CONNECTED, PVP_WAITING, PVE_CONNECTED, ALREADY_CONNECTED, ERR}

    private StartTalkResponseType responseType;
    private String anotherUser;

    /**
     * @param anotherUser   匹配到的用户的ID，只有PVP_CONNECTED时不为null
     */
    public StartTalkResponse(StartTalkResponseType responseType, String user, long responseTime, String anotherUser) {
        super(user, responseTime);
        this.responseType = responseType;
        this.anotherUser = anotherUser;
    }

    public StartTalkResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = StartTalkResponseType.ERR;
        this.anotherUser = null;
    }

    public StartTalkResponseType getResponseType() {
        return responseType;
    }

    public String getAnotherUser() {
        return anotherUser;
    }
}
