package nextstep.auth.principal;

public class UnknownUserPrincipal implements UserPrincipal {
    @Override
    public boolean isUnknown() {
        return true;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getRole() {
        return null;
    }
}
