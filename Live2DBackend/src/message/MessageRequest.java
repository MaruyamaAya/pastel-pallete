package message;

public class MessageRequest extends Request {
    public enum MessageRequestType {TALK, QUERY}

    private MessageRequestType requestType;
    private String text;
    private String voicePath;

    public MessageRequest(MessageRequestType requestType, String user, long requestTime,
                          String textOrVoicePath, boolean useText) {
        super(user, requestTime);
        this.requestType = requestType;
        if (useText) {
            this.text = textOrVoicePath;
            this.voicePath = null;
        }
        else {
            this.text = null;
            this.voicePath = textOrVoicePath;
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
