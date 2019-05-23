package server;

import backend.BackendMonitor;
import message.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Live2DServer {
    public static final int MESSAGE = 0;
    public static final int SIGN_IN = 1;
    public static final int SIGN_UP = 2;
    public static final int LOG_OUT = 3;
    public static final int START_TALK = 4;
    public static final int END_TALK = 5;

    private final BackendMonitor backendMonitor = new BackendMonitor(this);
    private boolean stop = false;
    Map<String, String> userUnique = new HashMap<>();
    private int uniqueCnt = 0;
    private int test = 0;

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
     * TODO: 异步发送消息给对应的客户端，同时处理在线用户的信息
     *
     * @param json json object
     */
    private synchronized void response(JSONObject json) {
        int responseType = json.getInt("response_type");
        String user = json.getString("user");
        String unique = json.getString("unique");
        int subResponseType = json.optInt("sub_response_type", -1);
        if (responseType == SIGN_IN && subResponseType == 1)
            userUnique.put(user, unique);
        if (responseType == LOG_OUT && subResponseType == 1)
            userUnique.remove(user);

        // TODO 以下为测试代码
        System.out.println(json);

    }
/*
json.put("unique", String.valueOf(uniqueCnt++));
json.put("user", "Li Goudan");
json.put("request_type", 0);
json.put("request_time", System.currentTimeMillis());
json.put("password", "123456");
json.put("start_talk_request_type", 0);
json.put("message_request_type", 0);
json.put("use_text", 1);
json.put("text", "ni hao");
json.put("voice_path", "");
 */

    /**
     * TODO: 在主线程中被循环调用，侦听从客户端发来的JSON数据并返回；如果收到连接请求，处理完后不返回，回到开头继续执行。
     *
     * @return JSON
     */
    private JSONObject request() {
        // TODO 以下为测试代码
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        switch (test) {
            case 0:
                System.out.println("---sign up LGD");
                json.put("unique", String.valueOf(uniqueCnt++));
                json.put("user", "Li Goudan");
                json.put("request_type", SIGN_UP);
                json.put("request_time", System.currentTimeMillis());
                json.put("password", "123456");
                break;
            case 1:
                System.out.println("---sign in with wrong psw");
                json.put("unique", String.valueOf(uniqueCnt++));
                json.put("user", "Li Goudan");
                json.put("request_type", SIGN_IN);
                json.put("request_time", System.currentTimeMillis());
                json.put("password", "12345");
                break;
            case 2:
                System.out.println("---sign in LGD");
                json.put("unique", String.valueOf(uniqueCnt++));
                json.put("user", "Li Goudan");
                json.put("request_type", SIGN_IN);
                json.put("request_time", System.currentTimeMillis());
                json.put("password", "123456");
                break;
            case 3:
                System.out.println("---sign in again");
                json.put("unique", String.valueOf(uniqueCnt++));
                json.put("user", "Li Goudan");
                json.put("request_type", SIGN_IN);
                json.put("request_time", System.currentTimeMillis());
                json.put("password", "123456");
                break;
            case 4:
                System.out.println("---start talk with bot");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", START_TALK);
                json.put("request_time", System.currentTimeMillis());
                json.put("start_talk_request_type", 1);
                break;
            case 5:
                System.out.println("---message");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", MESSAGE);
                json.put("request_time", System.currentTimeMillis());
                json.put("message_request_type", 0);
                json.put("use_text", 1);
                json.put("text", "你是个笨蛋！");
                break;
            case 6:
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("---end talk with bot");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", END_TALK);
                json.put("request_time", System.currentTimeMillis());
                break;
            case 7:
                System.out.println("---end talk again");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", END_TALK);
                json.put("request_time", System.currentTimeMillis());
                break;
            case 8:
                System.out.println("---sign up with repeated name");
                json.put("unique", String.valueOf(uniqueCnt++));
                json.put("user", "Li Goudan");
                json.put("request_type", SIGN_UP);
                json.put("request_time", System.currentTimeMillis());
                json.put("password", "123456");
                break;
            case 9:
                System.out.println("---sign up ZTZ");
                json.put("unique", String.valueOf(uniqueCnt++));
                json.put("user", "Zhao Tiezhu");
                json.put("request_type", SIGN_UP);
                json.put("request_time", System.currentTimeMillis());
                json.put("password", "123");
                break;
            case 10:
                System.out.println("---sign in ZTZ");
                json.put("unique", String.valueOf(uniqueCnt++));
                json.put("user", "Zhao Tiezhu");
                json.put("request_type", SIGN_IN);
                json.put("request_time", System.currentTimeMillis());
                json.put("password", "123");
                break;
            case 11:
                System.out.println("---start talk LGD");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", START_TALK);
                json.put("request_time", System.currentTimeMillis());
                json.put("start_talk_request_type", 0);
                break;
            case 12:
                System.out.println("---start talk ZTZ");
                json.put("unique", userUnique.get("Zhao Tiezhu"));
                json.put("user", "Zhao Tiezhu");
                json.put("request_type", START_TALK);
                json.put("request_time", System.currentTimeMillis());
                json.put("start_talk_request_type", 0);
                break;
            case 13:
                System.out.println("---message to LGD");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", MESSAGE);
                json.put("request_time", System.currentTimeMillis());
                json.put("message_request_type", 0);
                json.put("use_text", 0);
                json.put("voice_path", "E:/GitHub/pastel-pallete/Live2DBackend/scripts/test2.wav");
                break;
            case 14:
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("---end talk ZTZ");
                json.put("unique", userUnique.get("Zhao Tiezhu"));
                json.put("user", "Zhao Tiezhu");
                json.put("request_type", END_TALK);
                json.put("request_time", System.currentTimeMillis());
                break;
            case 15:
                System.out.println("---end talk LGD");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", END_TALK);
                json.put("request_time", System.currentTimeMillis());
                break;
            case 16:
                System.out.println("---log out LGD");
                json.put("unique", userUnique.get("Li Goudan"));
                json.put("user", "Li Goudan");
                json.put("request_type", LOG_OUT);
                json.put("request_time", System.currentTimeMillis());
                break;
            default:
                stop = true;
        }
        test++;
        return json;
    }

    /**
     * TODO: 对于一个在线的用户，返回其客户端标识；若user为null或""，返回null
     *
     * @param user an online user
     * @return unique id of the user, null if user is invalid
     */
    public String getUserUnique(String user) {
        return userUnique.get(user);
    }

    public static void main(String[] args) {
        Live2DServer server = new Live2DServer();
        server.work();
    }
}
