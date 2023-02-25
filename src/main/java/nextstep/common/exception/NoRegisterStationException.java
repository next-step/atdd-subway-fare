package nextstep.common.exception;

import nextstep.common.error.SubwayError;

public class NoRegisterStationException extends RuntimeException {

    public NoRegisterStationException(final SubwayError subwayError) {
        super(subwayError.getMessage());
    }
}