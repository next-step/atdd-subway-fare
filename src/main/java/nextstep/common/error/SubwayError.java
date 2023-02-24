package nextstep.common.error;

public enum SubwayError {
    NO_DELETE_ONE_SECTION("노선의 구간 목록수가 1개인 경우 삭제할 수 없습니다."),
    NO_REGISTER_LINE_STATION("요청한 역이 노선의 등록되어 있지 않습니다."),
    NO_FIND_SAME_SOURCE_TARGET_STATION("출발역과 도착역이 같아서 조회가 불가능합니다."),
    NO_PATH_CONNECTED("출발역과 도착역이 연결되어 있지 않아서 조회가 불가능합니다."),
    NOT_FOUND("존재하지 않습니다. 입력한 정보를 확인해주세요."),
    ;

    private final String message;

    SubwayError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
