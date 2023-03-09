package nextstep.member.domain;

import java.util.List;

public class AnonymousUser extends AuthenticatedUser {
    public static final String PRINCIPAL = Long.toString(-1L);
    public static final List<String> ROLES = List.of(RoleType.ROLE_ANONYMOUS.name());
}
