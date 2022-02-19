package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

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
        return Fare.distance(fareParams.getDistance());
    }

}
