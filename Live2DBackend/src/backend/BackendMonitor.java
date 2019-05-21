package backend;

import message.*;
import server.Live2DServer;

public class BackendMonitor {
    private final UserDatabase userDatabase = new UserDatabase();
    private final Live2DServer server;

    public BackendMonitor(Live2DServer server) {
        this.server = server;
    }

    public String receiveSignUpRequest(String unique, SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = userDatabase.signUp(signUpRequest);
        server.sendResponse(unique, signUpResponse);
        return null;
    }

    public String receiveSignInRequest(String unique, SignInRequest signInRequest) {
        SignInResponse signInResponse = userDatabase.signIn(signInRequest);
        server.sendResponse(unique, signInResponse);
        return null;
    }

    public String receiveLogOutRequest(String unique, LogOutRequest logOutRequest) {
        LogOutResponse logOutResponse = userDatabase.logOut(logOutRequest);
        server.sendResponse(unique, logOutResponse);
        return null;
    }

    public String receiveStartTalkRequest(String unique, StartTalkRequest startTalkRequest) {
        StartTalkResponse startTalkResponse = userDatabase.startTalk(startTalkRequest);
        server.sendResponse(unique, startTalkResponse);
        StartTalkResponse.StartTalkResponseType responseType = startTalkResponse.getResponseType();
        if (responseType == StartTalkResponse.StartTalkResponseType.PVP_CONNECTED) {
            StartTalkResponse startTalkResponse2 = new StartTalkResponse(
                    StartTalkResponse.StartTalkResponseType.PVP_CONNECTED,
                    startTalkResponse.getAnotherUser(),
                    startTalkResponse.getResponseTime(),
                    startTalkResponse.getUser());
            server.sendResponse(server.getUserUnique(startTalkResponse2.getUser()), startTalkResponse2);
        }
        return null;
    }

    public String receiveEndTalkRequest(String unique, EndTalkRequest endTalkRequest) {
        EndTalkResponse endTalkResponse = userDatabase.endTalk(endTalkRequest);
        server.sendResponse(unique, endTalkResponse);
        EndTalkResponse.EndTalkResponseType responseType = endTalkResponse.getResponseType();
        if (responseType == EndTalkResponse.EndTalkResponseType.PASSED) {
            String anotherUser = endTalkResponse.getAnotherUser();
            if (!anotherUser.equals("")) {
                EndTalkResponse endTalkResponse2 = new EndTalkResponse(
                        EndTalkResponse.EndTalkResponseType.ANOTHER_ENDED,
                        anotherUser,
                        endTalkResponse.getResponseTime(),
                        null
                );
                server.sendResponse(server.getUserUnique(anotherUser), endTalkResponse2);
            }
        }
        return null;
    }

    public String receiveMessageRequest(String unique, MessageRequest messageRequest) {
        String user = messageRequest.getUser();
        int inputType = messageRequest.getInputType();
        int reqType;
        String inputVal = messageRequest.getInputValue();
        MessageRequest.MessageRequestType messageRequestType = messageRequest.getRequestType();
        UserStatus userStatus = userDatabase.getUserStatus(user);
        if (userStatus == UserStatus.PVP_TALKING)
            reqType = 0;
        else if (userStatus == UserStatus.PVE_TALKING
                && messageRequestType == MessageRequest.MessageRequestType.TALK)
            reqType = 1;
        else if (userStatus == UserStatus.PVE_TALKING
                && messageRequestType == MessageRequest.MessageRequestType.QUERY)
            reqType = 2;
        else
            return userStatus.toString();

        MessageToSend messageToSend = new MessageToSend(user, inputType, reqType, inputVal);
        MessageReceived messageReceived = PythonScriptsMonitor.messageProcess(messageToSend);

        // TODO
        if (messageReceived.hasBot()) {
            long responseTime = messageReceived.getResponseTime();
            String vp1 = messageReceived.getVoicePath();
            String vp2 = messageReceived.getBotVoicePath();
            int gi1 = GestureChooser.chooseGesture(messageReceived.getEmotion());
            int gi2 = GestureChooser.chooseGesture(messageReceived.getBotEmotion());
            MessageResponse mr1 = new MessageResponse(user, responseTime, false, vp1, gi1);
            MessageResponse mr2 = new MessageResponse(user, responseTime, true, vp2, gi2);
            server.sendResponse(unique, mr1);
            server.sendResponse(unique, mr2);
        } else {
            String anotherTalker = userDatabase.anotherTalkerOf(user);
            if (anotherTalker == null)
                return "User " + user + " is not talking to anyone at all!";
            if (anotherTalker.equals(""))
                return "User " + user + " is talking to robot but seems PVP.";
            long responseTime = messageReceived.getResponseTime();
            String vp = messageReceived.getVoicePath();
            int gi = GestureChooser.chooseGesture(messageReceived.getEmotion());
            MessageResponse mr1 = new MessageResponse(user, responseTime, false, vp, gi);
            MessageResponse mr2 = new MessageResponse(anotherTalker, responseTime, true, vp, gi);
            server.sendResponse(server.getUserUnique(anotherTalker), mr1);
            server.sendResponse(unique, mr2);
        }

        return null;
    }

    public static void main(String[] args) {
        BackendMonitor backendMonitor = new BackendMonitor(new Live2DServer());
//        MessageRequest messageRequest = new MessageRequest(
//                MessageRequest.MessageRequestType.TALK,
//                "test1",
//                System.currentTimeMillis(),
//                "E:/JavaProjects/Live2DBackend/scripts/test_bot.wav",
//                false);
        MessageRequest messageRequest = new MessageRequest(
                MessageRequest.MessageRequestType.TALK,
                "test2",
                System.currentTimeMillis(),
                "你可真是个铁憨憨！",
                "",
                true);
        backendMonitor.receiveMessageRequest("sfafasd", messageRequest);
    }
}
