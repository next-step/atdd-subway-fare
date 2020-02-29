package atdd.user.application.dto;

public class LoginResponseView {
    private String accessToken;
    private String tokenType;

    public LoginResponseView(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
