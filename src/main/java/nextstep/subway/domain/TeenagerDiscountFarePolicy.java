package nextstep.subway.domain;

public class TeenagerDiscountFarePolicy extends AbstractFarePolicy {

    private static final double DISCOUNT = 0.8;

    public TeenagerDiscountFarePolicy() {
        this(null);
    }

    public TeenagerDiscountFarePolicy(FarePolicy nextPolicy) {
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
