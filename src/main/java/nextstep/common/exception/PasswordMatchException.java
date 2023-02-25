package nextstep.common.exception;

import nextstep.common.error.MemberError;

public class PasswordMatchException extends RuntimeException {

    public PasswordMatchException(final MemberError memberError) {
        super(memberError.getMessage());
    }
}