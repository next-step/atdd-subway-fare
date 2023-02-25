package nextstep.subway.domain;

public class Fare {
    private final FarePolicy farePolicy;

    public Fare() {
        this(new BaseFarePolicy());
    }

    public Fare(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public int calculate(int distance) {
        return farePolicy.calculate(distance);
    }
}
