package message;

public class MessageRequest extends Request {
    public enum MessageRequestType {
        TALK, QUERY;
        public static MessageRequestType fromIndex(int i) {
            switch (i) {
                case 0: return TALK;
                case 1: return QUERY;
                default: return TALK;
            }
        }
    }

    private MessageRequestType requestType;
    private String text;
    private String voicePath;

    public MessageRequest(MessageRequestType requestType, String user, long requestTime,
                          String text, String voicePath, boolean useText) {
        super(user, requestTime);
        this.requestType = requestType;
        if (useText) {
            this.text = text;
            this.voicePath = null;
        }
        else {
            this.text = null;
            this.voicePath = voicePath;
        }
    }

    public int getInputType() {
        if (voicePath == null)
            return 1;
        return 0;
    }

    public String getInputValue() {
        if (voicePath != null)
            return voicePath;
        return text;
    }

    public MessageRequestType getRequestType() {
        return requestType;
    }
}
