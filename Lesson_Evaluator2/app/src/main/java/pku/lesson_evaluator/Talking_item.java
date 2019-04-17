package pku.lesson_evaluator;

public class Talking_item {
    private String talkingId;
    private String talkingTime;
    private String talkingContent;

    public Talking_item(String talkingId,String talkingTime,String talkingContent){
        this.talkingId=talkingId;
        this.talkingTime=talkingTime;
        this.talkingContent=talkingContent;
    }

    public String getTalkingContent() {
        return talkingContent;
    }

    public String getTalkingId() {
        return talkingId;
    }

    public String getTalkingTime() {
        return talkingTime;
    }
}
