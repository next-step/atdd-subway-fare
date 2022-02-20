package nextstep.exception.fare;

import nextstep.exception.ServiceException;

public class FareStrategyException extends ServiceException {

    private static final String MESSAGE = "요금 정책 설정이 필요합니다.";

    public FareStrategyException() {
        super(MESSAGE);
    }

}
