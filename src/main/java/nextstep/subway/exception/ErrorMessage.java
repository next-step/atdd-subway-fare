package nextstep.subway.exception;

public enum ErrorMessage {
	SHOULD_BE_PROVIDED_EXISTED_STATION("존재하는 지하철역이 제공되어야 합니다."),
	SHOULD_BE_DIFFERENT_SOURCE_AND_TARGET("출발역과 도찰역은 달라야 합니다.");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
