package nextstep.subway.maps.map.domain;

public class FareContext {
    public static final int DEFAULT_FARE = 1250;
    private final int distance;
    private int fare;

    public FareContext(int distance) {
        this.distance = distance;
        this.fare = DEFAULT_FARE;
    }

    public int getDistance() {
        return this.distance;
    }

    public void plusFare(int fare) {
        this.fare += fare;
    }

    public void discountFare(int discount) {
        if (this.fare <= discount) {
            this.fare = 0;
            return;
        }

        this.fare -= discount;
    }

    public void discountPercent(int percent) {
        if (percent <= 0 || percent > 100) {
            throw new IllegalArgumentException("0 < percent <= 100");
        }

        this.fare = (int) Math.floor(this.fare * ((100-percent)/100f));
    }

    public int getFare() {
        return this.fare;
    }
}
