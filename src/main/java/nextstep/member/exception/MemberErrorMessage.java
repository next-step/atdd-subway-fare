package nextstep.member.exception;

public enum MemberErrorMessage {
	UNAUTHENTICATION_USER("비인증 유저입니다.");

	private final String message;

	MemberErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
