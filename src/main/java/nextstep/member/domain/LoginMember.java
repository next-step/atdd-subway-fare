package nextstep.member.domain;

import java.util.List;

public class LoginMember {
    private Long id;
    private int age;
    private List<String> roles;

    public LoginMember(final Long id, final List<String> roles) {
        this(id, 0, roles);
    }

    public LoginMember(final Long id,
                       final int age,
                       final List<String> roles) {
        this.id = id;
        this.age = age;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public List<String> getRoles() {
        return roles;
    }
}
