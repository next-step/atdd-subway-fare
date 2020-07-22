package nextstep.subway.members.member.domain;


public class EmptyMember extends LoginMember {

    public static final String EMPTY = "EMPTY";

    public EmptyMember() {
        super(0L, EMPTY, EMPTY, 0);
    }
}
