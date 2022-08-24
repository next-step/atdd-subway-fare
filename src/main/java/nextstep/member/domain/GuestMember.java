package nextstep.member.domain;

public class GuestMember extends Member {
    private GuestMember() {

    }

    public static GuestMember create() {
        return new GuestMember();
    }

    @Override
    public Integer getAge() {
        return Integer.MAX_VALUE;
    }
}
