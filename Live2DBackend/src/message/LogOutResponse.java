package message;

import org.json.JSONObject;
import server.Live2DServer;

public class LogOutResponse extends Response {
    public enum LogOutResponseType {
        PASSED, ALREADY_LOG_OUT, ERR;
        public int toIndex() {
            switch (this) {
                case ERR: return 0;
                case PASSED: return 1;
                case ALREADY_LOG_OUT: return 2;
                default: return 0;
            }
        }
    }

    private LogOutResponseType responseType;

    public LogOutResponse(LogOutResponseType responseType, String user, long responseTime) {
        super(user, responseTime);
        this.responseType = responseType;
    }

    public LogOutResponse(String user, String errMsg, long responseTime) {
        super(user, errMsg, responseTime);
        this.responseType = LogOutResponseType.ERR;
    }

    public LogOutResponseType getResponseType() {
        return responseType;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("response_type", Live2DServer.LOG_OUT);
        json.put("sub_response_type", responseType.toIndex());
        return json;
    }
}
