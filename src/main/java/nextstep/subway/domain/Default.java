package nextstep.subway.domain;

public class DefaultDiscount extends DiscountFarePolicy {

    public DefaultDiscount(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return getFare();
    }

}
