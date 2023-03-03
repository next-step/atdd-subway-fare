package nextstep.member.domain;

import java.util.List;

public class Guest extends LoginMember {
    public Guest() {
        super(0L, List.of());
    }

    @Override
    public boolean isGuest() {
        return true;
    }
}
