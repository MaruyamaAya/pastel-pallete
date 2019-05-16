package server;

import backend.BackendMonitor;
import message.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Live2DServer {
    private final BackendMonitor backendMonitor = new BackendMonitor(this);
    private final Map<String, List<JSONObject>> userResponsesBuffer = new HashMap<>();

    // TODO
    public Live2DServer() {

    }

    /**
     * 服务器主线程
     */
    private void work() {

    }

    /**
     * 发送sign up信息，成功返回null，否则返回错误说明
     * @param signUpResponse    the response to send
     * @return                  error msg, null if no error
     */
    public String sendSignUpResponse(SignUpResponse signUpResponse) {
        return null;
    }

    public String sendSignInResponse(SignInResponse signInResponse) {
        return null;
    }

    public String sendLogOutResponse(LogOutResponse logOutResponse) {
        return null;
    }

    public String sendStartTalkResponse(StartTalkResponse startTalkResponse) {
        return null;
    }

    public String sendEndTalkResponse(EndTalkResponse endTalkResponse) {
        return null;
    }

    public String sendMessageResponse(MessageResponse messageResponse) {
        messageResponse.print();
        return null;
    }

    private void response(String user, JSONObject jsonObject) {
        // TODO
    }

    // END TODO

    public static void main(String[] args) {
        Live2DServer server = new Live2DServer();
        server.work();
    }
}
