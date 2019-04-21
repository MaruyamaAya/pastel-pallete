package message;

public class StartTalkRequest extends Request {
    public enum StartTalkRequestType {PVP, PVE}

    private StartTalkRequestType requestType;

    public StartTalkRequest(StartTalkRequestType requestType, String user, long requestTime) {
        super(user, requestTime);
        this.requestType = requestType;
    }

    public StartTalkRequestType getRequestType() {
        return requestType;
    }
}
