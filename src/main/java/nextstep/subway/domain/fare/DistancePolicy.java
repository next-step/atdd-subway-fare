package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.FareType;

public class DistancePolicy implements FarePolicy {

    private DistancePolicy() {
    }

    private static class DistancePolicyHolder {
        private static final DistancePolicy INSTANCE = new DistancePolicy();
    }

    public static DistancePolicy getInstance() {
        return DistancePolicy.DistancePolicyHolder.INSTANCE;
    }

    @Override
    public Fare apply(Fare fare, FareParams fareParams) {
        int distanceFare = FareType.fare(fareParams.getDistance());
        return Fare.distance(distanceFare);
    }

}
