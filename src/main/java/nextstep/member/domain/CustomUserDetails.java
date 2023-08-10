package nextstep.member.domain;

import nextstep.auth.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private String email;
    private String password;
    private String role;
    private int age;

    public CustomUserDetails(String email, String password, String role, Integer age) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.age = age;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public int getAge() {
        return age;
    }
}
