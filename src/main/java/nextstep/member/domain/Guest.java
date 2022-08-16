package nextstep.member.domain;

public class Guest extends Member {

    @Override
    public boolean isChildren() {
        return false;
    }

    @Override
    public boolean isTeenager() {
        return false;
    }
}
