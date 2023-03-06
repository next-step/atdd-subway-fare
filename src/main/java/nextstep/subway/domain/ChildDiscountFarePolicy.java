package nextstep.subway.domain;

public class ChildDiscountFarePolicy extends AbstractFarePolicy {

    private static final double DISCOUNT = 0.5;

    public ChildDiscountFarePolicy() {
        this(null);
    }

    public ChildDiscountFarePolicy(FarePolicy nextPolicy) {
        super(nextPolicy);
    }

    @Override
    public int calculateFare(int fare) {
        int result = (int) Math.floor(fare * DISCOUNT);

        if (hasNext()) {
            return this.nextPolicy.calculateFare(result);
        }

        return result;
    }
}
