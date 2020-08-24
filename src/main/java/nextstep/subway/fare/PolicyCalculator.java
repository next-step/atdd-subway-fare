package nextstep.subway.fare;

public class PolicyCalculator {
    private final DiscountPolicy discountPolicy;

    public PolicyCalculator(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public int calculate(int fare, int extraFare) {
        return discountPolicy.calculateDiscountAmount(fare, extraFare);
    }


}
