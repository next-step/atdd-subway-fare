package nextstep.subway.util;

public class DiscountAdult implements DiscountAgePolicy {

    @Override
    public int discount(int fare) {
        return 0;
    }
}
