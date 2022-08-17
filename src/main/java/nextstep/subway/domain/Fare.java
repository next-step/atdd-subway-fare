package nextstep.subway.domain;

public class Fare {
    private int totalDistance;
    private int additionalFare;
    private DiscountPolicy discountPolicy;
    private FarePolicy farePolicy;

    public Fare(int totalDistance, int additionalFare, DiscountPolicy discountPolicy) {
        this.totalDistance = totalDistance;
        this.additionalFare = additionalFare;
        this.discountPolicy = discountPolicy;
        this.farePolicy = FarePolicy.of(totalDistance);
    }

    public Fare(int totalDistance) {
        this(totalDistance, 0, DiscountPolicy.ADULT);
    }

    public int value() {
        return discountPolicy.discount(additionalFare + farePolicy.calculate(totalDistance));
    }
}
