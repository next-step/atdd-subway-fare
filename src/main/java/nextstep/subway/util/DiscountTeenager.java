package nextstep.subway.util;

public class DiscountTeenager implements DiscountAgePolicy {

    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public int discount(int fare) {
        return (int) (fare - ((fare - DEDUCTION_FARE) * DISCOUNT_RATE));
    }
}
