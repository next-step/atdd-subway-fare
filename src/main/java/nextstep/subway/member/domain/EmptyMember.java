package nextstep.subway.member.domain;

public class EmptyMember extends LoginMember {

    private static final String EMPTY_USER = "Empty";

    public EmptyMember() {
        super(null, EMPTY_USER, EMPTY_USER, 20);
    }
}
