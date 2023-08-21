package nextstep.subway.path.dto;

import nextstep.auth.principal.UserPrincipal;

public class UserDto {
    private final String email;
    private final String role;

    public static UserDto of(UserPrincipal userPrincipal) {
        return new UserDto(userPrincipal.getUsername(), userPrincipal.getRole());
    }

    public UserDto(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
