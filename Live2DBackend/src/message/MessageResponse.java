package message;

import org.json.JSONObject;
import server.Live2DServer;

public class MessageResponse extends Response {
    private boolean isAnother;
    private String voicePath;
    private int gestureId;

    public MessageResponse(String user, long responseTime,
                           boolean isAnother, String voicePath, int gestureId) {
        super(user, responseTime);
        this.isAnother = isAnother;
        this.voicePath = voicePath;
        this.gestureId = gestureId;
    }

    public boolean isAnother() {
        return isAnother;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public int getGestureId() {
        return gestureId;
    }

    @Override
    public void print() {
        System.out.println("MessageResponse---");
        super.print();
        System.out.println("isAnother:" + isAnother);
        System.out.println("voicePath:" + voicePath);
        System.out.println("gestureId:" + gestureId);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("response_type", Live2DServer.MESSAGE);
        if (isAnother)
            json.put("is_another", 1);
        else
            json.put("is_another", 0);
        json.put("voice_path", voicePath);
        json.put("gesture_id", gestureId);
        return json;
    }
}
