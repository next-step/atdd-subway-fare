package nextstep.subway.path.domain.discount;

public class DefaultDiscountPolicy implements DiscountPolicy {
    @Override
    public int discount(int totalFare) {
        return totalFare;
    }
}
