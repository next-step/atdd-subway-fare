package nextstep.common;

public enum ErrorMsg {
	PATH_DISTANCE_0_UNDER_VALUE("거리 0(km)이하입니다."),
	PATH_DISTANCE_WRONG_VALUE("거리 계산이 올바르지 않아 요금을 계산할 수 없습니다."),

	SECTION_DISTANCE_WRONG_VALUE("올바른 거리(km)가 아닙니다."),
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
