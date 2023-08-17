package nextstep.line.domain.fare;

import nextstep.exception.FareNotMatchException;

import java.util.List;

public class DistanceFarePolicies {

    private final List<DistanceFarePolicy> values;

    public DistanceFarePolicies() {
        this.values = List.of(new DefaultDistancePolicy(), new OverTenDistancePolicy(), new OverFiftyDistancePolicy());
    }

    public Integer getFare(int distance) {
        return this.values.stream()
                .filter(distanceFarePolicy -> distanceFarePolicy.isIncluded(distance))
                .map(distanceFarePolicy -> distanceFarePolicy.fare(distance))
                .findFirst()
                .orElseThrow(FareNotMatchException::new);
    }

}
