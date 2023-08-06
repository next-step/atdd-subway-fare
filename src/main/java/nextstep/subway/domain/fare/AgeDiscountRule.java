package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;
import org.springframework.util.ObjectUtils;

public class AgeDiscountRule implements FareCalculationRule {
    private FareCalculationRule nextRule;
    private static final int DEDUCTION_FEE = 350;

    @Override
    public void setNextRule(FareCalculationRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public int calculateFare(Path path, int age, int fare) {
        int discountedAmount = calculateDiscountedAmount(age, fare);

        if (ObjectUtils.isEmpty(nextRule)) {
            return discountedAmount;
        } else {
            return nextRule.calculateFare(path, age, discountedAmount);
        }
    }

    private int calculateDiscountedAmount(int age, int fare) {
        double discountAmount = 0;

        if (AgeDiscount.isDiscount(age)) {
            fare -= DEDUCTION_FEE;
            AgeDiscount ageDiscount = AgeDiscount.getByAge(age);
            discountAmount = fare * ageDiscount.getDiscountRate();
        }
        return (int) (fare - discountAmount);
    }
}
