package nextstep.common.exception;

import nextstep.common.error.MemberError;

public class EmailInputException extends RuntimeException {

    public EmailInputException(final MemberError memberError) {
        super(memberError.getMessage());
    }
}