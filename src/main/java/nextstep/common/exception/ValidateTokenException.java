package nextstep.common.exception;

import nextstep.common.error.MemberError;

public class ValidateTokenException extends RuntimeException {

    public ValidateTokenException(final MemberError memberError) {
        super(memberError.getMessage());
    }
}