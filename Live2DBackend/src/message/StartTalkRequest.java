package message;

public class StartTalkRequest extends Request {
    public enum StartTalkRequestType {
        PVP, PVE;
        public static StartTalkRequestType fromIndex(int i) {
            switch (i) {
                case 0: return PVP;
                case 1: return PVE;
                default: return PVP;
            }
        }
    }

    private StartTalkRequestType requestType;

    public StartTalkRequest(StartTalkRequestType requestType, String user, long requestTime) {
        super(user, requestTime);
        this.requestType = requestType;
    }

    public StartTalkRequestType getRequestType() {
        return requestType;
    }
}
