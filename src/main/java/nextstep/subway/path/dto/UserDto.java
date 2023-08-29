package nextstep.subway.path.dto;

import nextstep.auth.principal.UserPrincipal;

public class UserDto {
    private final boolean isUnknown;
    private final String email;
    private final String role;

    public static UserDto of(UserPrincipal userPrincipal) {
        return new UserDto(userPrincipal.isUnknown(), userPrincipal.getUsername(), userPrincipal.getRole());
    }

    private UserDto(boolean isUnknown, String email, String role) {
        this.isUnknown = isUnknown;
        this.email = email;
        this.role = role;
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
