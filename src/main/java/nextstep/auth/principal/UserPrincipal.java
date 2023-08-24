package nextstep.auth.principal;

public interface UserPrincipal {
    boolean isUnknown();

    String getUsername();

    String getRole();
}
