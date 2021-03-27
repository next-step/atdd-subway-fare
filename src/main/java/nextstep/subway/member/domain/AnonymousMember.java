package nextstep.subway.member.domain;

public class AnonymousMember extends LoginMember{

    public static final String ANONYMOUS = "ANONYMOUS";

    public AnonymousMember() {
        super(null, ANONYMOUS, ANONYMOUS, 20);
    }
}
