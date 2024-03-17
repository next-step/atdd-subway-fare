package nextstep.subway.domain;

import java.util.List;

public class FareCalculator {
    private final List<FarePolicy> policies;
    private int fare;

    public FareCalculator(Path path) {
        this.policies = List.of(new DistancePolicy(path.getDistance()), new LinePolicy(path.getSections()));
        this.fare = 1250;
        calculateFare();
    }

    private void calculateFare() {
        for(FarePolicy policy : policies) {
            fare += policy.getAdditionalFee();
            fare += policy.getDiscountFee();
        }
    }

    public int getFare() {
        return fare;
    }
}
