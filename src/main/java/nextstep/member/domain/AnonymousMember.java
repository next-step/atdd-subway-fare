package nextstep.member.domain;

import nextstep.auth.application.UserDetails;

public class AnonymousMember implements UserDetails {
    private String message;

    public AnonymousMember() {
    }

    public AnonymousMember(final String message) {
        this.message = message;
    }

    @Override
    public boolean isSamePassword(final String password) {
        return false;
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }

    public String getMessage() {
        return message;
    }
}
