package nextstep.subway.domain.policy.calculate;

public class FixedFarePolicy implements FareCalculatePolicy {

    private final int fare;

    public FixedFarePolicy(int fare) {
        this.fare = fare;
    }

    @Override
    public int calculate(CalculateConditions conditions) {
        return fare;
    }

}
