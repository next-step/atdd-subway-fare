package nextstep.auth.principal;

public class AnonymousPrincipal extends UserPrincipal {
    public AnonymousPrincipal() {
        super(null, null, null);
    }
}
