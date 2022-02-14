package nextstep.exception.line;

import nextstep.exception.ServiceException;

public class MinimumDistanceException extends ServiceException {

    private static final String MESSAGE = "구간의 거리 최소값은 1 입니다 - %d";

    public MinimumDistanceException(int distance) {
        super(String.format(MESSAGE, distance));
    }

}
