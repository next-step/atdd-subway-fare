package nextstep.subway.domain.fare.distance;

import nextstep.subway.domain.fare.distance.DistanceFarePolicy;

public class Exceed extends DistanceFarePolicy {

    public Exceed(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE + includeExtraFare() + exceedExtraFare();
    }

    private int includeExtraFare() {
        return (int) ((Math.ceil((applyIncludeDistance() - 1) / INCLUDE_PER_DISTANCE) + 1) * INCLUDE_EXTRA_FARE);
    }

    private int exceedExtraFare() {
        return (int) ((Math.ceil((applyExceedDistance() - 1) / EXCEED_PER_DISTANCE) + 1) * EXCEED_EXTRA_FARE);
    }

    private int applyExceedDistance() {
        return getDistance() - EXTRA_FARE_EXCEED_DISTANCE;
    }

    private int applyIncludeDistance() {
        return EXTRA_FARE_EXCEED_DISTANCE - EXTRA_FARE_INCLUDE_DISTANCE;
    }

}
