package nextstep.subway.path.domain.policy.age;

public class AgeFareYouthPolicy implements AgeFarePolicy {

    public static final int DEDUCTION_AMOUNT = 350;
    private static final double DISCOUNT = 0.8;

    private final int fare;

    public AgeFareYouthPolicy(int fare) {
        this.fare = fare;
    }

    @Override
    public int calculateAgeFare() {
        return (int) ((fare - DEDUCTION_AMOUNT) * DISCOUNT);
    }
}
