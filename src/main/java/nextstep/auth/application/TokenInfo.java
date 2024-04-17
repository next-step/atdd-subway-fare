package nextstep.auth.application;

public class TokenInfo {

    private String email;
    private Integer age;

    public TokenInfo(String email, Integer age) {
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

