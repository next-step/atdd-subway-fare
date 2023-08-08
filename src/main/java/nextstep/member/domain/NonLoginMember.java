package nextstep.member.domain;

import nextstep.auth.userdetails.UserDetails;

public class NonLoginMember implements UserDetails {
    @Override
    public String getUsername() {
        return "guest";
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getRole() {
        return RoleType.ROLE_GUEST.toString();
    }

    @Override
    public int getAge() {
        return 999;
    }
}
