package nextstep.member.domain;

import java.util.List;

public class LoginMember {
    private Long id;
    private List<String> roles;
    private int age;

    public LoginMember(Long id, List<String> roles, int age) {
        this.id = id;
        this.roles = roles;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public int getAge() {
        return age;
    }
}
