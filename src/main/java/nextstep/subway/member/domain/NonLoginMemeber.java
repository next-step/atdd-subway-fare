package nextstep.subway.member.domain;

public class NonLoginMemeber extends LoginMember{

    private static final String NONE = "none";
    private static final Integer DEFALT_AGE = 0;
    public NonLoginMemeber() {
        super(null, NONE, NONE, DEFALT_AGE);
    }
}
