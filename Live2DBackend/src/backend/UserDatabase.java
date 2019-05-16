package backend;

import message.*;

import java.util.List;

abstract class UserDatabase {
    // TODO implement
    abstract SignInResponse signIn(SignInRequest request);

    abstract SignUpResponse signUp(SignUpRequest request);

    abstract LogOutResponse logOut(LogOutRequest request);

    abstract StartTalkResponse startTalk(StartTalkRequest request);

    abstract EndTalkResponse endTalk(EndTalkRequest request);

    /**
     * 返回该用户的聊天对象。若未在聊天，返回null；若在与机器人聊天，返回""
     * @param user  user
     * @return      another talker, "" or null
     */
    abstract String anotherTalkerOf(String user);

    /**
     * 返回用户的当前状态
     * @param user  user to detect
     * @return      his status
     */
    abstract UserStatus getUserStatus(String user);
}
