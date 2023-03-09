package nextstep.member.context;

public class SecurityContext {

    private String authentication;
    public void setAuthentication(String token) {
        authentication = token;
    }

    public String getAuthentication() {
        return authentication;
    }
}
