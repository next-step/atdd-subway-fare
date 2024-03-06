package nextstep.auth.application;

import nextstep.member.domain.AgeType;

public class UserDetail {

    public static final UserDetail EMPTY = new UserDetail(null, null, null);

    protected String email;
    protected String password;
    protected Integer age;


    public UserDetail(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public boolean hasUserDetail() {
        return email != null && password != null;
    }

    public String getEmail() {
        return email;
    }

    public AgeType getAge() {
        return AgeType.getAgeType(this.age);
    }
}
