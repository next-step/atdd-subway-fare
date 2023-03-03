package nextstep.member.domain;

import java.util.List;

public class Guest implements User {

    @Override
    public Long getId() {
        throw new IllegalStateException("로그인 필요");
    }

    @Override
    public List<String> getRoles() {
        return List.of("ROLE_GUEST");
    }

    @Override
    public boolean isGuest() {
        return true;
    }
}
