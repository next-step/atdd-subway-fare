package nextstep.subway.domain;

public class Fare {
    public static final Fare ZERO = new Fare(0);
    private static final int DEDUCTION = 350;
    private static final double TEENAGERS_DISCOUNT_PERCENT = 0.2;
    private static final double CHILDREN_DISCOUNT_PERCENT = 0.5;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 음수가 될 수 없습니다.");
        }
        return new Fare(fare);
    }

    public Fare add(Fare other) {
        return new Fare(this.fare + other.fare);
    }

    public Fare teenagersDiscountFare() {
        int discountFare = (int) (((double) (fare - DEDUCTION)) * TEENAGERS_DISCOUNT_PERCENT);
        return new Fare(fare - discountFare);
    }

    public Fare childrenDiscountFare() {
        int discountFare = (int) (((double) (fare - DEDUCTION)) * CHILDREN_DISCOUNT_PERCENT);
        return new Fare(fare - discountFare);
    }

    public int getFare() {
        return fare;
    }
}
