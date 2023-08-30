package nextstep.subway.path.domain.policy.discount;

public class AgeDiscountPolicy implements DiscountPolicy {
    private static final int DEDUCTION_AMOUNT = 350;

    public int discount(int age, int totalFare) {
        if (6 <= age && age < 13) {
            return (int) (DEDUCTION_AMOUNT + (totalFare - DEDUCTION_AMOUNT) * 0.5);
        }

        if (13 <= age && age < 19) {
            return (int) (DEDUCTION_AMOUNT + (totalFare - DEDUCTION_AMOUNT) * 0.8);
        }

        return totalFare;
    }
}
