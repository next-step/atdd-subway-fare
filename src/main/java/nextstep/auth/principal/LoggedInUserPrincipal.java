package nextstep.auth.principal;

public class LoggedInUserPrincipal extends UserPrincipal {
    public LoggedInUserPrincipal(String username, String role) {
        super(username, role);
    }
}
