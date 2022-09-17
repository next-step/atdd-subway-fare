package nextstep.subway.domain;

import nextstep.subway.domain.fare.distance.DistanceFarePolicy;

public class Include extends DistanceFarePolicy {

    public Include(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE + includeExtraFare();
    }

    private int includeExtraFare() {
        return (int) ((Math.ceil((applyIncludeDistance() - 1) / INCLUDE_PER_DISTANCE) + 1) * INCLUDE_EXTRA_FARE);
    }

    private int applyIncludeDistance() {
        return getDistance() - EXTRA_FARE_INCLUDE_DISTANCE;
    }


}
