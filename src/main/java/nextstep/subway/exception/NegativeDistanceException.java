package nextstep.subway.exception;

public class NegativeDistanceException extends RuntimeException {
	private static final ErrorMessage DEFAULT_ERROR_MESSAGE = ErrorMessage.SHOULD_BE_POSITIVE;

	public NegativeDistanceException() {
		super(DEFAULT_ERROR_MESSAGE.getMessage());
	}
}
