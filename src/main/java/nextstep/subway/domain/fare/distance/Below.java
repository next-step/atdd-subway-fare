package nextstep.subway.domain.fare.distance;

import nextstep.subway.domain.fare.distance.DistanceFarePolicy;

public class Below extends DistanceFarePolicy {

    public Below(int distance) {
        super(distance);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE;
    }

}
