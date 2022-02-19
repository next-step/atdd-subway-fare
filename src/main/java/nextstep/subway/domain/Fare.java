package nextstep.subway.domain;

public class Fare {

    private static final int FREE_OF_FARE = 0;

    private final int distanceFare;
    private int extraCharge;
    private int discountFare;

    private Fare(int distanceFare) {
        this.distanceFare = distanceFare;
    }

    public static Fare free() {
        return new Fare(FREE_OF_FARE);
    }

    public static Fare distance(int distanceFare) {
        return new Fare(distanceFare);
    }

    public Fare extraCharge(int extraCharge) {
        this.extraCharge = extraCharge;
        return this;
    }

    public Fare discount(int discountFare) {
        this.discountFare = discountFare;
        return this;
    }

    public int totalFare() {
        return distanceFare + extraCharge - discountFare;
    }

    public int getDistanceFare() {
        return distanceFare;
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    public int getDiscountFare() {
        return discountFare;
    }
}
