package nextstep.member.domain;

import java.util.Arrays;
import java.util.List;
import support.auth.userdetails.User;

public class Guest extends User {
    private static final String GUEST_EMAIL = "guest@email.com";
    private static final int GUEST_AGE = 20;
    private static final List<String> ROLES = Arrays.asList(RoleType.ROLE_GUEST.name());

    public static final Guest guestUser = new Guest();
    public static final Member guestMember = new Member(GUEST_EMAIL, null, GUEST_AGE, ROLES);

    private Guest() {
        super(GUEST_EMAIL, null, ROLES);
    }
}
