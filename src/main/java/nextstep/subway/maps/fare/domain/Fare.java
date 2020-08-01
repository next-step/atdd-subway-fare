package nextstep.subway.maps.fare.domain;

public class Fare {
    private int fare;

    public Fare(int fare) {
        this.fare = fare;
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

        this.fare = (int) Math.floor(this.fare * ((100 - percent) / 100f));
    }

    public int getValue() {
        return this.fare;
    }
}
