package nextstep.member.domain;

import nextstep.exception.UnAuthorizedException;

public class Guest implements User {
    @Override
    public Long getId() {
        throw new UnAuthorizedException("로그인 후 시도하세요.");
    }

    @Override
    public String getEmail() {
        throw new UnAuthorizedException("로그인 후 시도하세요.");
    }

    @Override
    public String getPassword() {
        throw new UnAuthorizedException("로그인 후 시도하세요.");
    }

    @Override
    public int getAge() {
        throw new UnAuthorizedException("로그인 후 시도하세요.");
    }

    @Override
    public boolean isGuest() {
        return true;
    }

    @Override
    public int applyFarePolicy(int fare) {
        return fare;
    }
}

