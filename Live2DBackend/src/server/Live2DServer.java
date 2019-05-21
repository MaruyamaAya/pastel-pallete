package server;

import backend.BackendMonitor;
import message.*;
import org.json.JSONObject;


public class Live2DServer {
    public static final int MESSAGE = 0;
    public static final int SIGN_IN = 1;
    public static final int SIGN_UP = 2;
    public static final int LOG_OUT = 3;
    public static final int START_TALK = 4;
    public static final int END_TALK = 5;

    private final BackendMonitor backendMonitor = new BackendMonitor(this);
    private boolean stop = false;

    /**
     * TODO: 服务器的初始化
     */
    public Live2DServer() {

    }

    /**
     * 服务器主线程
     */
    private void work() {
        while (!stop) {
            JSONObject json = request();
            Thread thread = new Thread(() -> process(json));
            thread.start();
        }
    }

    /**
     * 向unique对应的客户端发送信息，成功返回null，否则返回错误说明
     *
     * @param unique   unique id
     * @param response the response to send
     * @return error msg, null if no error
     */
    public String sendResponse(String unique, Response response) {
        if (unique == null)
            return "unique is null";
        JSONObject json = response.toJson();
        json.put("unique", unique);
        response(json);
        return null;
    }

    /*
        'unique': '192.168.0.1/sdfasfsdgfgaasa13f53s4fsa5',
        'user': 'Li Goudan',
        'request_type': 0,
        'request_time': 1315615321231,
        'password': '',
        'start_talk_request_type': 0,
        'message_request_type': 0,
        'use_text': 1,
        'text': '你好',
        'voice_path': ''
     */
    private void process(JSONObject json) {
        if (json == null) {
            System.err.println("PROCESS ERROR: null json");
            return;
        }
        String unique = json.optString("unique");
        String user = json.optString("user");
        int requestType = json.optInt("request_type", -1);
        long requestTime = json.optLong("request_time", -1L);
        String password = json.optString("password");
        int startTalkRequestType = json.optInt("start_talk_request_type", -1);
        int messageRequestType = json.optInt("message_request_type", -1);
        int useText = json.optInt("use_text", -1);
        String text = json.optString("text");
        String voicePath = json.optString("voice_path");

        String err = "req type err";
        switch (requestType) {
            case MESSAGE:
                MessageRequest messageRequest = new MessageRequest(
                        MessageRequest.MessageRequestType.fromIndex(messageRequestType),
                        user,
                        requestTime,
                        text,
                        voicePath,
                        useText == 1
                );
                err = backendMonitor.receiveMessageRequest(unique, messageRequest);
                break;
            case SIGN_IN:
                SignInRequest signInRequest = new SignInRequest(user, requestTime, password);
                err = backendMonitor.receiveSignInRequest(unique, signInRequest);
                break;
            case SIGN_UP:
                SignUpRequest signUpRequest = new SignUpRequest(user, requestTime, password);
                err = backendMonitor.receiveSignUpRequest(unique, signUpRequest);
                break;
            case LOG_OUT:
                LogOutRequest logOutRequest = new LogOutRequest(user, requestTime);
                err = backendMonitor.receiveLogOutRequest(unique, logOutRequest);
                break;
            case START_TALK:
                StartTalkRequest startTalkRequest = new StartTalkRequest(
                        StartTalkRequest.StartTalkRequestType.fromIndex(startTalkRequestType),
                        user,
                        requestTime
                );
                err = backendMonitor.receiveStartTalkRequest(unique, startTalkRequest);
                break;
            case END_TALK:
                EndTalkRequest endTalkRequest = new EndTalkRequest(user, requestTime);
                err = backendMonitor.receiveEndTalkRequest(unique, endTalkRequest);
                break;
            default:
        }
        if (err != null) {
            System.err.println("ERROR WHILE PROCESSING: " + err);
        }
    }

    /**
     * TODO: 异步发送消息给对应的客户端
     *
     * @param json      json object
     */
    private synchronized void response(JSONObject json) {

    }

    /**
     * TODO: 在主线程中被循环调用，侦听从客户端发来的JSON数据并返回
     *
     * @return JSON
     */
    private JSONObject request() {
        return null;
    }

    /**
     * TODO: 对于一个在线的用户，返回其客户端标识；若user为null或""，返回null
     *
     * @param user an online user
     * @return unique id of the user, null if user is invalid
     */
    public String getUserUnique(String user) {
        return null;
    }

    public static void main(String[] args) {
        Live2DServer server = new Live2DServer();
        server.work();
    }
}
