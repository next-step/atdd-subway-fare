package nextstep.subway.domain.fare;

public class Fare {

    private int fare;
    private static final int STANDARD_FARE = 1_250;

    public static Fare standard() {
        return new Fare(STANDARD_FARE);
    }

    private Fare(int fare) {
        this.fare = fare;
    }

    public void add(int value) {
        fare += value;
    }

    public void change(int value) {
        fare = value;
    }

    public int getFare() {
        return fare;
    }
}
