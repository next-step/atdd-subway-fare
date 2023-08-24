package nextstep.auth.principal;

public class LoggedInUserPrincipal implements UserPrincipal {
    private final String username;
    private final String role;

    public LoggedInUserPrincipal(String username, String role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getRole() {
        return role;
    }
}
