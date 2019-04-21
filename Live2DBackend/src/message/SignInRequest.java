package message;

public class SignInRequest extends Request {
    private String password;

    public SignInRequest(String user, long requestTime, String password) {
        super(user, requestTime);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
