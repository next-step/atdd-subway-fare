package nextstep.common.exception;

import nextstep.common.error.SubwayError;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final SubwayError memberError) {
        super(memberError.getMessage());
    }
}