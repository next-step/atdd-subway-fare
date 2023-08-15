package nextstep.exception;

import static nextstep.exception.SubwayError.FARE_NOT_MATH_POLICY;

public class FareNotMatchException extends SubwayException {
    public FareNotMatchException() {
        super(FARE_NOT_MATH_POLICY);
    }
}
