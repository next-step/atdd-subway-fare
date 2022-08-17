package nextstep.member.domain;

public class GuestMember extends Member {
    private GuestMember() {

    }

    public static GuestMember create() {
        return new GuestMember();
    }

    @Override
    public boolean isChildren() {
        return false;
    }

    @Override
    public boolean isTeenage() {
        return false;
    }
}
