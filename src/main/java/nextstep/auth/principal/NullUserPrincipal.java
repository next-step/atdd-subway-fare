package nextstep.auth.principal;

public class NullUserPrincipal implements LoginUserPrincipal {
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getRole() {
        return null;
    }
}
