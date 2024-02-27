package nextstep.path.fare;

public class FareCalculator {
    private Fare fare;
    private int distance;

    public FareCalculator(int distance) {
        this.distance = distance;

        if (distance <= 10) {
            fare = new BaseFare();
        } else if (distance <= 50) {
            fare = new MediumFare();
        } else {
            fare = new LongFare();
        }
    }

    public int calculate() {
        return fare.calculate(distance);
    }
}
