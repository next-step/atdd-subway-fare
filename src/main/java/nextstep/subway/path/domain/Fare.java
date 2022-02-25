package nextstep.subway.path.domain;

public class Fare {

    private int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public void add(int fare) {
        this.fare += fare;
    }

    public int getValue() {
        return this.fare;
    }
}
