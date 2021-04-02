package nextstep.subway.path.domain.policy.age;

public class AgeFareYouthPolicy implements AgeFarePolicy {

    public static final int DEDUCTION_AMOUNT = 350;
    private static final double DISCOUNT = 0.8;

    @Override
    public int calculateAgeFare(int fare) {
        return (int) ((fare - DEDUCTION_AMOUNT) * DISCOUNT);
    }
}
