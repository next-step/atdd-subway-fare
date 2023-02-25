package nextstep.common.exception;

import nextstep.common.error.SubwayError;

public class NoDeleteOneSectionException extends RuntimeException {

    public NoDeleteOneSectionException(final SubwayError subwayError) {
        super(subwayError.getMessage());
    }
}