package nextstep.subway.path.domain;

public class Fare {

    private int fare;

    public Fare(int distance) {
        if(distance < 15) {
            fare = 1250;
        }
    }

    public int getFare() {
        return fare;
    }
}
