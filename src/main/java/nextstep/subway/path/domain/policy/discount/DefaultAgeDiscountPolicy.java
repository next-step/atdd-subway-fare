package nextstep.subway.path.domain.policy.discount;

public class DefaultAgeDiscountPolicy extends AgeDiscountPolicy {
    @Override
    public int discount(int totalFare) {
        return totalFare;
    }
}
