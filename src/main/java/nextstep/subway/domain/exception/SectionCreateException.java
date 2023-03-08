package nextstep.subway.domain.exception;

public class SectionCreateException extends IllegalArgumentException {

    public static final String MESSAGE = "구간을 생성할 수 없습니다.";

    public SectionCreateException() {
        super(MESSAGE);
    }
}
