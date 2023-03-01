package nextstep.subway.domain.exception;

public class MinimumDurationException extends RuntimeException {

    public MinimumDurationException() {
        super("최소 소요시간이 1분 이여야합니다.");
    }

}
