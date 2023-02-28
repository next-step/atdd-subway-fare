package nextstep.subway.domain.fare;

public class InvalidDistanceException extends RuntimeException {
    private static final String MESSAGE = "서울 지하철의 총 거리는 344km를 넘지 않습니다.";

    public InvalidDistanceException() {
        super(MESSAGE);
    }
}
