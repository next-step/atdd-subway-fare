package nextstep.subway.util.discount;

public class Adult implements DiscountAgePolicy {

    @Override
    public int discount(int fare) {
        return fare;
    }
}
