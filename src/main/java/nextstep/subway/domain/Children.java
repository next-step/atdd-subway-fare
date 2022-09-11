package nextstep.subway.domain;

public class Youth extends DiscountFarePolicy {

    public Youth() {
        super();
    }

    @Override
    public double discountRatio() {
        return 0.8;
    }

    @Override
    public int discountFare() {
        return 350;
    }

}
