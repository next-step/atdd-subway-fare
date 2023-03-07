package nextstep.member.domain;

import java.util.List;
import java.util.Objects;

public class LoginMember extends AuthenticatedUser {
    private Long id;
    private List<String> roles;

    public LoginMember(Long id, List<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginMember that = (LoginMember) o;
        return Objects.equals(id, that.id) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roles);
    }
}
