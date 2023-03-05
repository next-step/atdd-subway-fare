package nextstep.member.domain;

import java.util.Collections;
import java.util.List;

public class AuthMember {
    private Long id;
    private List<String> roles;

    public AuthMember(Long id, List<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    public static AuthMember toGuest() {
        return new AuthMember(null, Collections.emptyList());
    }

    public Long getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }
}
