package nextstep.common.exception;

import nextstep.common.error.MemberError;

public class MissingTokenException extends RuntimeException {

    public MissingTokenException(final MemberError memberError) {
        super(memberError.getMessage());
    }
}