package nextstep.auth.principal;

public class LoginMember {
    private Long id;
    private String email;

    private int age;

    public LoginMember(Long id, String email, int age) {
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

    public int getAge() {
        return age;
    }
}
