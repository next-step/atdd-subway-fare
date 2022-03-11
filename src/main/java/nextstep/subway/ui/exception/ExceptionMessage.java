package nextstep.subway.ui.exception;

public enum ExceptionMessage {
    NO_CONNECTION_START_AND_END_STATION("출발역과 도착역이 연결되어 있지 않습니다."),
    NOT_EXISTS_STATIONS_IN_LINE("노선에 등록되지 않은 역입니다."),
    NOT_EXISTS_EMAIL("존재하지 않는 이메일입니다."),
    INVALID_PASSWORD("잘못된 비밀번호입니다."),
    REQUIRE_SIGN_IN("로그인이 필요하거나 재로그인을 해주세요"),
    SAME_STATION("출발역과 도착역을 다르게 설정해주세요."),
    SECTION_LESS_THAN_ONE("구간이 1개 이하인 경우 삭제할 수 없습니다."),
    NOT_EXISTS_STATION_IN_SECTION("구간에 존재하지 않는 역입니다."),
    ADD_SECTION_DISTANCE("새로 추가되는 구간 거리는 기존 구간의 거리 이상일 수 없습니다. 기존 구간 거리 = %d, 신규 구간 거리 = %d"),
    EXISTS_STATION_IN_SECTION("이미 구간에 등록된 역입니다."),
    ADDITION_FARE_MIN("추가 요금은 최소 0원 이상이어야 합니다.");

    private final String msg;
    ExceptionMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
