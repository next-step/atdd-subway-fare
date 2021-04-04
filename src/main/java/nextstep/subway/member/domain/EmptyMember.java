package nextstep.subway.member.domain;

public class EmptyMember extends LoginMember {

	private static final String EMPTY_INFO = "empty";
	private static final int DEFAULT_AGE = 20;

	public EmptyMember() {
		super(null, EMPTY_INFO, EMPTY_INFO, DEFAULT_AGE);
	}
}
