package nextstep.subway.path.domain;

public class Fare {

    public static final int DEFAULT_FARE_DISTANCE = 10;
    public static final Fare DEFAULT_FARE = new Fare(1250);

    int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }
}
