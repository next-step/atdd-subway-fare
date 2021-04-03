package nextstep.subway.member.domain;

public class EmptyMember extends LoginMember {

    private static final String EMPTY_USER = "Empty";

    private EmptyMember() {
        super(null, EMPTY_USER, EMPTY_USER, 20);
    }

    public static EmptyMember getInstance() {
        return EmptyMemberLazyHolder.INSTANCE;
    }

    private static class EmptyMemberLazyHolder {
        private static final EmptyMember INSTANCE = new EmptyMember();
    }
}
