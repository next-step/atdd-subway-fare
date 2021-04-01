package nextstep.subway.path.domain.policy.age;

import static nextstep.subway.path.domain.policy.age.AgeFareYouthPolicy.DEDUCTION_AMOUNT;

public class AgeFareChildPolicy implements AgeFarePolicy {

    private static final double DISCOUNT = 0.5;

    private final int fare;

    public AgeFareChildPolicy(int fare) {
        this.fare = fare;
    }

    @Override
    public int calculateAgeFare() {
        return (int) ((fare - DEDUCTION_AMOUNT) * DISCOUNT);
    }
}
