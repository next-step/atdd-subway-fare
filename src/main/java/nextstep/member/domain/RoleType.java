package nextstep.member.domain;

import nextstep.member.exception.NotSupportedRoleTypeException;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum RoleType {
    ROLE_ADMIN,
    ROLE_MEMBER,
    ROLE_UNKNOWN;

    private static final Map<String, RoleType> NAME_TO_ROLE_TYPE = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(Enum::name, roleType -> roleType));

    public static RoleType of(String role) {
        if (!NAME_TO_ROLE_TYPE.containsKey(role)) {
            throw new NotSupportedRoleTypeException();
        }

        return NAME_TO_ROLE_TYPE.get(role);
    }

    public static boolean isUnknown(String userRole) {
        return ROLE_UNKNOWN == of(userRole);
    }
}
