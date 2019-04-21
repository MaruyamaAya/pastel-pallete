package message;

public class SignUpResponse extends Response {
    public enum SignUpResponseType {PASSED, USER_REPEATED, ERR}

    private SignUpResponseType responseType;

    public SignUpResponse(SignUpResponseType responseType, String user, long responseTime) {
        super(user, responseTime);
        this.responseType = responseType;
    }

    public SignUpResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = SignUpResponseType.ERR;
    }

    public SignUpResponseType getResponseType() {
        return responseType;
    }
}
