package nextstep.subway.domain;

public class Children extends DiscountFarePolicy {

    public Children() {
        super();
    }

    @Override
    public double discountRatio() {
        return 0.5;
    }

    @Override
    public int discountFare() {
        return 350;
    }

}
