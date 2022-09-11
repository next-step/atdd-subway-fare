package nextstep.subway.domain;

import static nextstep.subway.domain.OverFareType.INCLUDE_OVER_FARE;

public class IncludeDistance extends DistanceFarePolicy {

    public IncludeDistance(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        (int) ((Math.ceil((getDistance() - (MINIMUM_DISTANCE + 1)) / type.getPerDistance()) + 1) * type.getAddFare());
        return DEFAULT_FARE + calculateOverFare(getDistance(), INCLUDE_OVER_FARE);
    }

}
