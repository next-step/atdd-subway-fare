package nextstep.common.exception;

import nextstep.common.error.SubwayError;

public class SameStationException extends RuntimeException {

    public SameStationException(final SubwayError subwayError) {
        super(subwayError.getMessage());
    }
}
