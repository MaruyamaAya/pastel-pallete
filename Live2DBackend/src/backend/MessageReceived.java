package backend;

import org.json.JSONObject;

public class MessageReceived {
    private String errMsg;
    private String voicePath;
    private Emotion emotion;
    private long responseTime;
    private boolean hasBot;
    private String botVoicePath;
    private Emotion botEmotion;

    private MessageReceived() {
        this.errMsg = "exception";
        this.voicePath = null;
        this.emotion = null;
        this.responseTime = System.currentTimeMillis();
        this.hasBot = false;
        this.botVoicePath = null;
        this.botEmotion = null;
    }

    static MessageReceived fromJsonString(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        MessageReceived messageReceived = new MessageReceived();
        messageReceived.errMsg = jsonObject.getString("err");
        messageReceived.voicePath = jsonObject.getString("voice_path");
        messageReceived.emotion = Emotion.fromJSONObject(jsonObject.getJSONObject("emotion"));
        messageReceived.hasBot = jsonObject.getInt("has_bot") == 1;
        if (messageReceived.hasBot) {
            JSONObject bot = jsonObject.getJSONObject("bot");
            messageReceived.botVoicePath = bot.getString("voice_path");
            messageReceived.botEmotion = Emotion.fromJSONObject(bot.getJSONObject("emotion"));
        }
        return messageReceived;
    }

    static MessageReceived err() {
        return new MessageReceived();
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public boolean hasBot() {
        return hasBot;
    }

    public String getBotVoicePath() {
        return botVoicePath;
    }

    public Emotion getBotEmotion() {
        return botEmotion;
    }

    void print() {
        System.out.println(errMsg);
        System.out.println(voicePath);
        emotion.print();
        System.out.println(hasBot);
        System.out.println(botVoicePath);
        botEmotion.print();
    }
}
