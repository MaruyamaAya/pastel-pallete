package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public class Emotion {
    enum EmotionLevel1 {OPTIMISTIC, NEUTRAL, PESSIMISTIC}
    enum EmotionLevel2 {LIKE, HAPPY, ANGRY, DISGUSTING, FEARFUL, SAD, NOT_AVAILABLE}

    EmotionLevel1 level1;
    EmotionLevel2 level2;
    double[] prob;

    private Emotion(String level1, String level2, double[] prob) {
        EmotionLevel1 l1 = EmotionLevel1.NEUTRAL;
        switch (level1) {
            case "optimistic":
                l1 = EmotionLevel1.OPTIMISTIC;
                break;
            case "pessimistic":
                l1 = EmotionLevel1.PESSIMISTIC;
                break;
            default:
        }

        EmotionLevel2 l2 = EmotionLevel2.NOT_AVAILABLE;
        switch (level2) {
            case "like":
                l2 = EmotionLevel2.LIKE;
                break;
            case "happy":
                l2 = EmotionLevel2.HAPPY;
                break;
            case "angry":
                l2 = EmotionLevel2.ANGRY;
                break;
            case "disgusting":
                l2 = EmotionLevel2.DISGUSTING;
                break;
            case "fearful":
                l2 = EmotionLevel2.FEARFUL;
                break;
            case "sad":
                l2 = EmotionLevel2.SAD;
                break;
            default:
        }

        this.level1 = l1;
        this.level2 = l2;
        this.prob = prob;
    }

    public static Emotion fromJSONObject(JSONObject jsonObject) {
        String level1 = jsonObject.getString("l1");
        String level2 = jsonObject.getString("l2");
        JSONArray array = jsonObject.getJSONArray("prob");
        double[] prob = new double[]{array.getDouble(0), array.getDouble(1), array.getDouble(2)};
        return new Emotion(level1, level2, prob);
    }

    void print() {
        System.out.println(level1);
        System.out.println(level2);
        for (double d : prob) {
            System.out.println(d);
        }
    }
}
