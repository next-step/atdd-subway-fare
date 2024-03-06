package nextstep.member.domain;

public class LoginMember {
    private String email;
    private Integer age;

    public LoginMember(final String email) {
        this.email = email;
    }

    public LoginMember(final String email, final Integer age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
