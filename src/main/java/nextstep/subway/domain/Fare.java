package nextstep.subway.domain;

public class Fare {

    private static final int FREE_OF_FARE = 0;

    private final int distanceFare;
    private int extraCharge;
    private int discountFare;
    private int totalFare;

    private Fare(int distanceFare) {
        this.distanceFare = distanceFare;
        this.totalFare = distanceFare;
    }

    public static Fare free() {
        return new Fare(FREE_OF_FARE);
    }

    public static Fare distance(int distanceFare) {
        return new Fare(distanceFare);
    }

    public Fare extraCharge(int extraCharge) {
        this.extraCharge = extraCharge;
        this.totalFare = this.totalFare + extraCharge;
        return this;
    }

    public Fare discount(int discountFare) {
        this.discountFare = discountFare;
        this.totalFare = this.totalFare - discountFare;
        return this;
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

    public int getTotalFare() {
        return totalFare;
    }
}
