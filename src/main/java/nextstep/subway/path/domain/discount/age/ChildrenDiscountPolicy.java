package nextstep.subway.path.domain.discount.age;

public class ChildrenDiscountPolicy extends AgeDiscountPolicy {
    @Override
    public int discount(int totalFare) {
        return (int) (DEDUCTION_AMOUNT + (totalFare - DEDUCTION_AMOUNT) * 0.5);
    }
}
