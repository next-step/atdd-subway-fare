package nextstep.subway.domain.exception;

public class NotFoundMaxExtraFeeException extends RuntimeException {

    public NotFoundMaxExtraFeeException() {
        super("가장 높은 추가 운임 금액을 찾지 못했습니다.");
    }

}
