package nextstep.subway.domain.fare;

public class Fare {
    private static final int ZERO_FARE = 0;

    private final int totalFare;

    public Fare() {
        this.totalFare = ZERO_FARE;
    }

    public Fare(int totalFare) {
        validateMinusFare(totalFare);
        this.totalFare = totalFare;
    }

    public Fare addFare(int extraFare) {
        return new Fare(this.totalFare + extraFare);
    }

    public Fare discountFare(int discountFare) {
        return new Fare(this.totalFare - discountFare);
    }

    private void validateMinusFare(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("계산하고자 하는 요금 값은 음수일 수 없습니다.");
        }
    }

    public int extraTotalFare() {
        return this.totalFare;
    }
}
