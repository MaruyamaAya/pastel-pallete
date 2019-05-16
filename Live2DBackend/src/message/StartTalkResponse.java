package message;

import org.json.JSONObject;
import server.Live2DServer;

public class StartTalkResponse extends Response {
    public enum StartTalkResponseType {
        PVP_CONNECTED, PVP_WAITING, PVE_CONNECTED, ALREADY_CONNECTED, ERR;
        public int toIndex() {
            switch (this) {
                case ERR: return 0;
                case PVP_CONNECTED: return 1;
                case PVP_WAITING: return 2;
                case PVE_CONNECTED: return 3;
                case ALREADY_CONNECTED: return 4;
                default: return 0;
            }
        }
    }

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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("response_type", Live2DServer.START_TALK);
        json.put("sub_response_type", responseType.toIndex());
        json.put("another_user", anotherUser);
        return json;
    }
}
