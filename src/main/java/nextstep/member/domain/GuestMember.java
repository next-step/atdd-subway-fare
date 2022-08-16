package nextstep.member.domain;

public class GuestMember extends Member {

    private static class GuestMemberHolder {
        private static final GuestMember INSTANCE = new GuestMember();
    }

    public static GuestMember getInstance() {
        return GuestMemberHolder.INSTANCE;
    }
}
