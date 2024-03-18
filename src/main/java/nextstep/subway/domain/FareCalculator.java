package nextstep.subway.domain;

import nextstep.member.domain.entity.Member;

import java.util.List;

public class FareCalculator {
    private final List<FarePolicy> policies;
    private int fare;

    public FareCalculator(Path path) {
        this.policies = List.of(new DistancePolicy(path.getDistance()), new LinePolicy(path.getSections()));
        this.fare = 1250;
        calculateFare();
    }

    public FareCalculator(Path path, Member member) {
        this.policies = List.of(new DistancePolicy(path.getDistance()), new LinePolicy(path.getSections()), new AgePolicy(member.getAge()));
        this.fare = 1250;
        calculateFare();
    }

    private void calculateFare() {
        for(FarePolicy policy : policies) {
            fare += policy.getAdditionalFee();
            fare -= policy.getDiscountFee();
            fare *= 1 - policy.getDiscountPercent();
        }
    }

    public int getFare() {
        return fare;
    }
}
