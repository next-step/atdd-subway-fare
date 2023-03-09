package nextstep.subway.domain.exception;

public class LineAdditionalFareException extends IllegalArgumentException {
    private static final String MESSAGE = "노선 추가 요금이 설정되지 않았습니다.";

    public LineAdditionalFareException() {
        super(MESSAGE);
    }
}
