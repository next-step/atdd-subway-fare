package nextstep.path.exception;


import nextstep.common.exception.ValidationError;

public class FareApplyingException extends ValidationError {
    public FareApplyingException(final String message) {
        super(message);
    }
}
