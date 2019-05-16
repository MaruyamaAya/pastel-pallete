package message;

import org.json.JSONObject;

public abstract class Response {
    private String user;
    private String errMsg;
    private long responseTime;

    Response(String user, long responseTime) {
        this.user = user;
        this.errMsg = "";
        this.responseTime = responseTime;
    }

    Response(String user, String errMsg, long responseTime) {
        this.user = user;
        this.errMsg = errMsg;
        this.responseTime = responseTime;
    }

    public String getUser() {
        return user;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void print() {
        System.out.println("Response---");
        System.out.println("user:" + user);
        System.out.println("errMsg:" + errMsg);
        System.out.println("responseTime:" + responseTime);
    }

    /*
    'user': 'Li Goudan',
    'response_time': 1315615324565,
    'response_type': 0,
    'sub_response_type': 0,
    'another_user': '',
    'is_another': 0,
    'voice_path': 'https://data.pastel-palette.com/voice/ags23154gfda1g231.wav',
    'gesture_id': 3
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("user", user);
        json.put("response_time", responseTime);
        json.put("response_type", -1);
        json.put("sub_response_type", -1);
        json.put("another_user", "");
        json.put("is_another", -1);
        json.put("voice_path", "");
        json.put("gesture_id", -1);
        return json;
    }
}
