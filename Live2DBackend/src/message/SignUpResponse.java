package message;

import org.json.JSONObject;
import server.Live2DServer;

public class SignUpResponse extends Response {
    public enum SignUpResponseType {
        PASSED, USER_REPEATED, ERR;
        public int toIndex() {
            switch (this) {
                case ERR: return 0;
                case PASSED: return 1;
                case USER_REPEATED: return 2;
                default: return 0;
            }
        }
    }

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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("response_type", Live2DServer.SIGN_UP);
        json.put("sub_response_type", responseType.toIndex());
        return json;
    }
}
