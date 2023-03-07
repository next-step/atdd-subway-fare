package nextstep.subway.domain.fare;

import java.util.List;

public class Fare {
    private final List<FarePolicy> farePolicies;

    public Fare() {
        this.farePolicies = List.of(new BasicFare(), new OverTenKmFare(), new OverFiftyKmFare());
    }

    public int calculateFare(int distance) {
        return farePolicies.stream().mapToInt(farePolicy -> farePolicy.calculateOverFare(distance)).sum();
    }
}
