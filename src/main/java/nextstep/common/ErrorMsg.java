package nextstep.common;

public enum ErrorMsg {
	SECTION_DURATION_WRONG_VALUE("올바른 소요시간이 아닙니다.")
	;

	private final String message;

	ErrorMsg(String message) {
		this.message = message;
	}

	public String isMessage() {
		return message;
	}
}
