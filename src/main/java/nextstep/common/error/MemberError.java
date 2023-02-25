package nextstep.common.error;

public enum MemberError {
    UNAUTHORIZED("인증에 실패하였습니다. 입력한 정보를 확인해주세요."),
    NOT_VALID_TOKEN("유효하지 않은 토큰입니다."),
    NOT_INPUT_EMAIL("입력한 Email을 확인해주세요."),
    NOT_MISSING_TOKEN("토큰 정보가 없습니다."),
    ;

    private final String message;

    MemberError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
