package message;

public class SignUpRequest extends Request{
    private String password;

    public SignUpRequest(String user, long requestTime, String password) {
        super(user, requestTime);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
