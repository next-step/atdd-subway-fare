package nextstep.subway.member.domain;

public class EmptyMember extends LoginMember{

    private static final String EMPTY_INFO = "empty";
    private static final int AGE = 0;

    private EmptyMember() {
        super(null, EMPTY_INFO, EMPTY_INFO, AGE);
    }

    private static EmptyMember emptyMember = new EmptyMember();

    public static EmptyMember getInstance() {
        return emptyMember;
    }
}
