package atdd.login.application.dto;

public class LoginResponseView {

    private String tokenType;
    private String accessToken;

    protected LoginResponseView() {
    }

    public LoginResponseView(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
