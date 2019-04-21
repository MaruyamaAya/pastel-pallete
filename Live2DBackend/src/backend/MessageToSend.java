package backend;

public class MessageToSend {
    private String user;
    private int inputType;
    private int reqType;
    private String inputVal;

    MessageToSend(String user, int inputType, int reqType, String inputVal) {
        this.user = user;
        this.inputType = inputType;
        this.reqType = reqType;
        this.inputVal = inputVal;
    }

    String toJsonString() {
        return "{\"user_id\": \"" + user + "\", " +
                "\"input_type\": " + inputType + ", " +
                "\"req_type\": " + reqType + ", " +
                "\"input_val\": \"" + inputVal + "\"}";
    }
}
