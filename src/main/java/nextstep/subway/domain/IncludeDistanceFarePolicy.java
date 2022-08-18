package nextstep.subway.domain;

import static nextstep.subway.domain.OverFareType.INCLUDE_OVER_FARE;

public class IncludeDistanceFarePolicy extends DistanceFarePolicy {

    public IncludeDistanceFarePolicy(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE + calculateOverFare(getDistance(), INCLUDE_OVER_FARE);
    }

}
