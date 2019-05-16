package message;

import org.json.JSONObject;
import server.Live2DServer;

public class EndTalkResponse extends Response {
    public enum EndTalkResponseType {
        PASSED, ANOTHER_ENDED, ALREADY_ENDED, ERR;
        public int toIndex() {
            switch (this) {
                case ERR: return 0;
                case PASSED: return 1;
                case ANOTHER_ENDED: return 2;
                case ALREADY_ENDED: return 3;
                default: return 0;
            }
        }
    }

    private EndTalkResponseType responseType;
    private String anotherUser;

    public EndTalkResponse(EndTalkResponseType responseType, String user, long responseTime, String anotherUser) {
        super(user, responseTime);
        this.responseType = responseType;
        this.anotherUser = anotherUser;
    }

    public EndTalkResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = EndTalkResponseType.ERR;
        this.anotherUser = null;
    }

    public EndTalkResponseType getResponseType() {
        return responseType;
    }

    public String getAnotherUser() {
        return anotherUser;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("response_type", Live2DServer.END_TALK);
        json.put("sub_response_type", responseType.toIndex());
        return json;
    }
}
