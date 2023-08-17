package nextstep.subway.domain;

import static nextstep.subway.domain.Fare.ADDITIONAL_FARE;
import static nextstep.subway.domain.Fare.MAX_OVER_TEN_FARE;

public class CalculateOverTen {

    public int calculate(int distance) {
        int overTenDistance = distance - 10;
        int fare = (int) ((Math.ceil((double) overTenDistance / 5)) * ADDITIONAL_FARE);
        return Math.min(fare, MAX_OVER_TEN_FARE);
    }
}
