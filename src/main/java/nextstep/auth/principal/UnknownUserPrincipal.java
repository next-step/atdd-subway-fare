package nextstep.auth.principal;

public class UnknownUserPrincipal extends UserPrincipal {
    public UnknownUserPrincipal() {
        super("", "ROLE_UNKNOWN");
    }
}
