package nextstep.member.domain;

import java.util.List;

public class LoginMember implements User {

    private Long id;
    private List<String> roles;

    public LoginMember(Long id, List<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public boolean isGuest() {
        return false;
    }
}
