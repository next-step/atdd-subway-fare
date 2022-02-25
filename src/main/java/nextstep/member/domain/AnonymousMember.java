package nextstep.member.domain;

import nextstep.auth.userdetails.UserDetails;

public class AnonymousMember implements UserDetails {
    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return false;
    }
}
