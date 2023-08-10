package nextstep.auth.principal;

import nextstep.member.domain.RoleType;

public class UserPrincipal {

    private static final String ANONYMOUS = "anonymous";
    private String username;
    private String role;

    public UserPrincipal(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public static UserPrincipal createAnonymous() {
        return new UserPrincipal(ANONYMOUS, RoleType.ROLE_ANONYMOUS.name());
    }

    public boolean isAnonymous() {
        return RoleType.isAnonymous(role);
    }
}
