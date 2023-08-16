package nextstep.member.domain;

import nextstep.auth.token.oauth2.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    private String email;
    private String role;
    private Integer age;

    public CustomOAuth2User(String email, String role, Integer age) {
        this.email = email;
        this.role = role;
        this.age = age;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public Integer getAge() {
        return age;
    }
}
