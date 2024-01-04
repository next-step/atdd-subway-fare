package nextstep.member.domain;

import nextstep.auth.token.oauth2.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    private String email;

    public CustomOAuth2User(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
