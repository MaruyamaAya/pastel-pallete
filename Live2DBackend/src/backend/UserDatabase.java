package backend;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NOTATIONDatatypeValidator;
import message.*;
import sun.awt.Symbol;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.util.*;

class UserDatabase {

    private Vector<String> userDataList;

    public UserDatabase() {
        String path = "./UserDatabase";
        File dbDir = new File(path);
        if (!dbDir.exists()) {
            dbDir.mkdir();
        }

        userDataList = new Vector();
        String userlist = "./UserDatabase/UserList.data";
        File dbFile = new File(userlist);
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            }
            catch (Exception e) {
                System.out.println("Error! Can't build user list file!");
            }
        }
        else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(dbFile));
                String tempString = null;
                while ((tempString = reader.readLine()) != null) {
                    userDataList.add(tempString);
                }
                reader.close();
            }
            catch (Exception e) {
                System.out.println("Error! Can't link to UserList");
            }
        }
    }

    SignInResponse signIn(SignInRequest request) {
        String username = request.getUser();
        String userpswd = request.getPassword();
        boolean check = false;
        for (String user : userDataList)
            if (user.equals(username)) {
                check = true;
                break;
            }
        if (check == false) {
            return new SignInResponse(SignInResponse.SignInResponseType.NO_SUCH_USER, username, System.currentTimeMillis());
        }
        UserData db = new UserData();
        db.setUser(username);
        db.pull();

        if (db.getStatus() != UserStatus.NOT_ONLINE)
            return new SignInResponse(SignInResponse.SignInResponseType.ERR, username, System.currentTimeMillis());
        if (!db.getPassword().equals(userpswd)) {
            return new SignInResponse(SignInResponse.SignInResponseType.INCORRECT_PASSWORD, username, System.currentTimeMillis());
        }

        db.setStatus(UserStatus.NOT_TALKING);
        db.push();
        return new SignInResponse(SignInResponse.SignInResponseType.PASSED, username, System.currentTimeMillis());
    }

    SignUpResponse signUp(SignUpRequest request) {
        String username = request.getUser();
        String userpswd = request.getPassword();
        boolean check = true;
        for (String user : userDataList)
            if (user.equals(username)) {
                check = false;
                break;
            }
        if (check == false) {
            return new SignUpResponse(SignUpResponse.SignUpResponseType.USER_REPEATED, username, System.currentTimeMillis());
        }
        UserData db = new UserData();
        userDataList.add(username);
        db.newUser(username, userpswd);

        return new SignUpResponse(SignUpResponse.SignUpResponseType.PASSED, username, System.currentTimeMillis());
    }

    LogOutResponse logOut(LogOutRequest request) {
        UserData db = new UserData();
        db.setUser(request.getUser());
        if (db.getStatus() == UserStatus.NOT_ONLINE)
            return new LogOutResponse(LogOutResponse.LogOutResponseType.ALREADY_LOG_OUT, request.getUser(), System.currentTimeMillis());
        db.setStatus(UserStatus.NOT_ONLINE);
        db.push();
        return new LogOutResponse(LogOutResponse.LogOutResponseType.PASSED, request.getUser(), System.currentTimeMillis());
    }

    StartTalkResponse startTalk(StartTalkRequest request) {
        UserData db = new UserData();
        db.setUser(request.getUser());
        StartTalkRequest.StartTalkRequestType requestType = request.getRequestType();
        if (db.getStatus() == UserStatus.PVP_TALKING) {
            if (requestType == StartTalkRequest.StartTalkRequestType.PVE)
                return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.ERR, request.getUser(), System.currentTimeMillis(), null);
            else
                return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.ALREADY_CONNECTED, request.getUser(), System.currentTimeMillis(), db.getAnother());
        }
        if (db.getStatus() == UserStatus.PVE_TALKING) {
            if (requestType == StartTalkRequest.StartTalkRequestType.PVP)
                return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.ERR, request.getUser(), System.currentTimeMillis(), db.getAnother());
            else
                return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.ALREADY_CONNECTED, request.getUser(), System.currentTimeMillis(), null);
        }
        if (db.getStatus() == UserStatus.WAITING) {
            return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.ERR, request.getUser(), System.currentTimeMillis(), null);
        }

        String userName = request.getUser();
        String anotherName = new String();
        if (requestType == StartTalkRequest.StartTalkRequestType.PVE) {
            db.setStatus(UserStatus.PVE_TALKING);
            db.setAnother("?robot");
            db.push();
            return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.PVE_CONNECTED, request.getUser(), System.currentTimeMillis(), null);
        }
        else if (requestType == StartTalkRequest.StartTalkRequestType.PVP) {
            db.setStatus(UserStatus.WAITING);
            db.push();
            boolean find = false;
            for (String user : userDataList) {
                UserData db2 = new UserData();
                db2.setUser(user);
                if (db2.getStatus() == UserStatus.WAITING) {
                    find = true;
                    anotherName = db2.getName();
                    db.setStatus(UserStatus.PVP_TALKING);
                    db2.setStatus(UserStatus.PVP_TALKING);
                    db.setAnother(anotherName);
                    db2.setAnother(userName);
                    db.push();
                    db2.push();
                    break;
                }
            }
            if (find == true) {
                return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.PVP_CONNECTED, userName, System.currentTimeMillis(), anotherName);
            }
            else {
                return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.PVP_WAITING, userName, System.currentTimeMillis(), null);
            }
        }
        return new StartTalkResponse(StartTalkResponse.StartTalkResponseType.ERR, request.getUser(), System.currentTimeMillis(), null);
    }

    EndTalkResponse endTalk(EndTalkRequest request) {
        UserData db = new UserData();
        db.setUser(request.getUser());
        String userName = request.getUser();
        if (db.getStatus() == UserStatus.PVE_TALKING) {
            db.setAnother("?null");
            db.setStatus(UserStatus.NOT_TALKING);
            db.push();
            return new EndTalkResponse(EndTalkResponse.EndTalkResponseType.ALREADY_ENDED, userName, System.currentTimeMillis(), null);
        }
        if (db.getStatus() != UserStatus.PVP_TALKING) {
            return new EndTalkResponse(EndTalkResponse.EndTalkResponseType.ALREADY_ENDED, userName, System.currentTimeMillis(), null);
        }
        String anotherName = db.getAnother();
        UserData db2 = new UserData();
        db2.setUser(anotherName);
        db.setStatus(UserStatus.NOT_TALKING);
        db2.setStatus(UserStatus.NOT_TALKING);
        db.setAnother("?none");
        db2.setAnother("?none");
        db.push();
        db2.push();
        return new EndTalkResponse(EndTalkResponse.EndTalkResponseType.PASSED, userName, System.currentTimeMillis(), anotherName);
    }

    String anotherTalkerOf(String user) {
        UserData db = new UserData();
        db.setUser(user);
        String anotherName = db.getAnother();
        if (anotherName.equals("?null"))
            return null;
        else if (anotherName.equals("?robot"))
            return "";
        else
            return db.getAnother();
    }

    UserStatus getUserStatus(String user) {
        UserData db = new UserData();
        db.setUser(user);
        return db.getStatus();
    }
}
