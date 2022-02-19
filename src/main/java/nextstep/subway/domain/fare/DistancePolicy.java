package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public class DistancePolicy implements FarePolicy {

    @Override
    public Fare apply(Fare fare, FareParams fareParams) {
        return Fare.distance(fareParams.getDistance());
    }

}
