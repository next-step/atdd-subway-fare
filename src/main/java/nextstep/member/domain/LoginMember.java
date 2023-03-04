package nextstep.member.domain;

import java.util.Collections;
import java.util.List;

public class LoginMember {
    private Long id;
    private List<String> roles;

    public LoginMember(Long id, List<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    public static LoginMember GUEST = new LoginMember(null, List.of());

    public Long getId() {
        return id;
    }

    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public boolean isGuest() {
        return this.equals(GUEST);
    }
}
