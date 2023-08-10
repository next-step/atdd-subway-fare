package nextstep.member.domain;

import java.util.Arrays;

public enum RoleType {
    ROLE_ADMIN,
    ROLE_MEMBER,
    ROLE_ANONYMOUS;

    public static boolean isAnonymous(String role) {
        boolean match = Arrays.stream(RoleType.values())
                .anyMatch(roleType -> roleType.name().equals(role));
        if (!match) {
            throw new RuntimeException();
        }
        return RoleType.valueOf(role).equals(ROLE_ANONYMOUS);
    }
}
