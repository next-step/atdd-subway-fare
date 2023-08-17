package nextstep.subway.domain;

import static nextstep.subway.domain.Fare.ADDITIONAL_FARE;

public class CalculateOverFifty {
    public int calculate(int distance) {
        int overFiftyDistance = distance - 50;
        if (overFiftyDistance <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) overFiftyDistance / 8) * ADDITIONAL_FARE;
    }
}
