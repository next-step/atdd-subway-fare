package nextstep.auth.principal;

public class UnknownUserPrincipal implements UserPrincipal {
    private static final String EMPTY = "";

    @Override
    public boolean isUnknown() {
        return true;
    }

    @Override
    public String getUsername() {
        return EMPTY;
    }

    @Override
    public String getRole() {
        return EMPTY;
    }
}
