package nextstep.subway.domain;

import nextstep.member.domain.Member;
import org.springframework.util.ObjectUtils;

public class AgeDiscountRule implements FareCalculationRule {
    private FareCalculationRule nextRule;
    private static final int DEDUCTION_FEE = 350;

    @Override
    public int calculateFare(Path path, Member member, int fare) {
        int resultAmount = fare;
        double discountAmount = 0;

        if (AgeDiscount.isDiscount(member.getAge())) {
            resultAmount -= DEDUCTION_FEE;
            AgeDiscount ageDiscount = AgeDiscount.getByAge(member.getAge());
            discountAmount = resultAmount * ageDiscount.getDiscountRate();
        }

        int totalFare = (int) (resultAmount - discountAmount);

        if (ObjectUtils.isEmpty(nextRule)) {
            return totalFare;
        } else {
            return nextRule.calculateFare(path, member, totalFare);
        }
    }

    @Override
    public void setNextRule(FareCalculationRule nextRule) {
        this.nextRule = nextRule;
    }
}
