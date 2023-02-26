package nextstep.subway.domain.policy;

public class FixedFarePolicy implements FarePolicy{

    private final int fare;

    public FixedFarePolicy(int fare) {
        this.fare = fare;
    }

    @Override
    public int calculate(int distance) {
        return fare;
    }

}
