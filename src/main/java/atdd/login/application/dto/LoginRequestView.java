package atdd.login.application.dto;

public class LoginRequestView {

    private String email;
    private String password;

    protected LoginRequestView() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
