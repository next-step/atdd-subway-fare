package nextstep.subway.path.domain.policy.age;

import static nextstep.subway.path.domain.policy.age.AgeFareYouthPolicy.DEDUCTION_AMOUNT;

public class AgeFareChildPolicy implements AgeFarePolicy {

    private static final double DISCOUNT = 0.5;

    @Override
    public int calculateAgeFare(int fare) {
        return (int) ((fare - DEDUCTION_AMOUNT) * DISCOUNT);
    }
}
