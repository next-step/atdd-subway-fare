package nextstep.subway.domain;

public class Fare {
    private int totalDistance;
    private int additionalFare;
    private AgeDiscountPolicy ageDiscountPolicy;

    public Fare(int totalDistance, int additionalFare, AgeDiscountPolicy ageDiscountPolicy) {
        this.totalDistance = totalDistance;
        this.additionalFare = additionalFare;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public Fare(int totalDistance) {
        this(totalDistance, 0, AgeDiscountPolicy.ADULT);
    }

    public int value() {
        return ageDiscountPolicy.discount(additionalFare + FarePolicy.calculateFare(totalDistance));
    }
}
