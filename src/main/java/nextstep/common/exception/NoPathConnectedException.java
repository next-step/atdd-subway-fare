package nextstep.common.exception;

import nextstep.common.error.SubwayError;

public class NoPathConnectedException extends RuntimeException {

    public NoPathConnectedException(final SubwayError subwayError) {
        super(subwayError.getMessage());
    }
}
