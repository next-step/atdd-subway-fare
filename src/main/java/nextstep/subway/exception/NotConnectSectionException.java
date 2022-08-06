package nextstep.subway.exception;

public class NotConnectSectionException extends IllegalArgumentException {

    public static final String EXCEPTION_MESSAGE = "not connect sections";

    public NotConnectSectionException() {
        super(EXCEPTION_MESSAGE);
    }
}
