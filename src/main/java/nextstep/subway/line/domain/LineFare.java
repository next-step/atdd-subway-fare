package nextstep.subway.line.domain;

public enum LineFare {
    ADULT(1250, 0),
    YOUTH(1250, 20),
    CHILD(1250, 50);

    private final int fare;
    private final int discountRate;

    LineFare(int fare, int discountRate) {
        this.fare = fare;
        this.discountRate = discountRate;
    }

    public int getFare() {
        return fare;
    }

    public int getDiscountRateOf(int fare) {
        double rate = discountRate / 100.0;
        return (int)(fare * rate);
    }
}
