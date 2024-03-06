package nextstep.auth.ui;

public class UserPrincipal {
    private final Long id;
    private final String email;
    private final Integer age;

    public UserPrincipal(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
