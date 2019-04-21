package backend;

import message.*;
import server.Live2DServer;

public class BackendMonitor {
    private final UserDatabase userDatabase = new UserDatabaseText();
    private final Live2DServer server;

    public BackendMonitor(Live2DServer server) {
        this.server = server;
    }

    public String receiveSignUpRequest(SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = userDatabase.signUp(signUpRequest);
        // TODO
        server.sendSignUpResponse(signUpResponse);
        return null;
    }

    public String receiveSignInRequest(SignInRequest signInRequest) {
        SignInResponse signInResponse = userDatabase.signIn(signInRequest);
        // TODO
        server.sendSignInResponse(signInResponse);
        return null;
    }

    public String receiveLogOutRequest(LogOutRequest logOutRequest) {
        LogOutResponse logOutResponse = userDatabase.logOut(logOutRequest);
        // TODO
        server.sendLogOutResponse(logOutResponse);
        return null;
    }

    public String receiveStartTalkRequest(StartTalkRequest startTalkRequest) {
        StartTalkResponse startTalkResponse = userDatabase.startTalk(startTalkRequest);
        // TODO
        server.sendStartTalkResponse(startTalkResponse);
        return null;
    }

    public String receiveEndTalkRequest(EndTalkRequest endTalkRequest) {
        EndTalkResponse endTalkResponse = userDatabase.endTalk(endTalkRequest);
        // TODO
        server.sendEndTalkResponse(endTalkResponse);
        return null;
    }

    public String receiveMessageRequest(MessageRequest messageRequest) {
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
            server.sendMessageResponse(mr1);
            server.sendMessageResponse(mr2);
        }
        else {
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
            server.sendMessageResponse(mr1);
            server.sendMessageResponse(mr2);
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
                true);
        backendMonitor.receiveMessageRequest(messageRequest);
    }
}
