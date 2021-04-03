package nextstep.subway.path.domain;

public class Fare {

    private int fare;

    public Fare(int distance) {
        calculate(distance);
    }

    private void calculate(int distance) {
        int fare = 1250;

        if(distance > 10) {
            fare += Math.min(calculateOverFare(distance - 10, 5), 800);
        }

        if(distance > 50) {
            fare += calculateOverFare(distance - 50, 8);
        }

        this.fare = fare ;
    }

    private int calculateOverFare(int distance, int offset) {
        return (int) ((Math.ceil((distance - 1) / offset) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
