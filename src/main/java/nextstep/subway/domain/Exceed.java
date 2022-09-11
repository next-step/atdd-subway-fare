package nextstep.subway.domain;

import static nextstep.subway.domain.OverFareType.EXCEED_OVER_FARE;
import static nextstep.subway.domain.OverFareType.INCLUDE_OVER_FARE;

public class ExceedDistance extends DistanceFarePolicy {

    public ExceedDistance(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE + calculateOverFare(MAXIMUM_DISTANCE, INCLUDE_OVER_FARE) + calculateOverFare(getDistance(), EXCEED_OVER_FARE);
    }

}
