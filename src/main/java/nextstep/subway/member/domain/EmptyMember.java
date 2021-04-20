package nextstep.subway.member.domain;

public class EmptyMember extends LoginMember {

    private static final String EMPTY_INFO = "empty";

    public EmptyMember() {
        super(null, EMPTY_INFO, EMPTY_INFO, 30);
    }
}
