package nextstep.member.domain;

import java.util.List;

public class LoginMember {
    private Long id;
    private int age;
    private List<String> roles;

    public LoginMember() {
    }

    public LoginMember(Long id, int age, List<String> roles) {
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
