package message;

import org.json.JSONObject;
import server.Live2DServer;

public class SignInResponse extends Response {
    public enum SignInResponseType {
        PASSED, NO_SUCH_USER, INCORRECT_PASSWORD, ERR;
        public int toIndex() {
            switch (this) {
                case ERR: return 0;
                case PASSED: return 1;
                case NO_SUCH_USER: return 2;
                case INCORRECT_PASSWORD: return 3;
                default: return 0;
            }
        }
    }

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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("response_type", Live2DServer.SIGN_IN);
        json.put("sub_response_type", responseType.toIndex());
        return json;
    }
}
