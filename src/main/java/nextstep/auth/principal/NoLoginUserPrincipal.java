package nextstep.auth.principal;

public class NoLoginUserPrincipal extends UserPrincipal {

    private static final String NO_LOGIN_USER = "NO_LOGIN_USER";
    private static final String NO_ROLE = "NO_ROLE";

    private String username;
    private String role;

    public NoLoginUserPrincipal() {
    }

    @Override
    public String getUsername() {
        return NO_LOGIN_USER;
    }

    @Override
    public String getRole() {
        return NO_ROLE;
    }

    @Override
    public boolean isLoginUser() {
        return false;
    }
}
