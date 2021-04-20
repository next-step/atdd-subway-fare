package nextstep.subway.path.domain;

public class Fare {

    private int fare;
    private final int basicFare = 1250;

    public Fare(){
        this.fare = basicFare;
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }
}