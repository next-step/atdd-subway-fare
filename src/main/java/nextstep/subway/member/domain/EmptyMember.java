package nextstep.subway.member.domain;

public class EmptyMember extends LoginMember{

    private static final String EMPTY_INFO = "empty";
    private static final int AGE = 0;

    public EmptyMember() {
        super(null, EMPTY_INFO, EMPTY_INFO, AGE);
    }
}
