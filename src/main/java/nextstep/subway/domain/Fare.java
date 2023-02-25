package nextstep.subway.domain;

public class Fare {
    private FarePolicy farePolicy;

    private Fare() {
    }

    public Fare(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public int calculate(int distance) {
        return farePolicy.calculate(distance);
    }
}
