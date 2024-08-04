package nextstep.subway.domain.entity;

import java.util.List;

public class SubWayFare {

    private static final int DEFAULT_FARE = 1250;
    private final List<FarePolicy> policies;
    private int fare;

    public SubWayFare(Path path) {
        this.policies = List.of(new DistanceFarePolicy(path.getDistance()));
        this.fare = DEFAULT_FARE;
    }

    public int calculateFare() {
        policies.forEach(policy -> fare += policy.getAdditionalFare());
        return fare;
    }
}
