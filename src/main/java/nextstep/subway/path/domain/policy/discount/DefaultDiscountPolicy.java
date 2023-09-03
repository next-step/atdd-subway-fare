package nextstep.subway.path.domain.policy.discount;

public class DefaultDiscountPolicy implements DiscountPolicy {
    @Override
    public int discount(int totalFare) {
        return totalFare;
    }
}
