package message;

public class SignInResponse extends Response {
    public enum SignInResponseType {PASSED, NO_SUCH_USER, INCORRECT_PASSWORD, ERR}

    private SignInResponseType responseType;

    public SignInResponse(SignInResponseType responseType, String user, long responseTime) {
        super(user, responseTime);
        this.responseType = responseType;
    }

    public SignInResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = SignInResponseType.ERR;
    }

    public SignInResponseType getResponseType() {
        return responseType;
    }
}
