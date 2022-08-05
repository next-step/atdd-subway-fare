package nextstep.subway.exception;

public class NotConnectSectionException extends IllegalArgumentException {
    public NotConnectSectionException() {
        super("not connect sections");
    }
}
