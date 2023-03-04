package nextstep.member.domain.exception;

import nextstep.common.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {

    NOT_FOUND_MEMBER("찾는 회원이 없습니다.");

    private final String message;

    MemberErrorCode(String message) {
        this.message = message;
    }


    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
