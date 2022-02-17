package nextstep.subway.domain;

import java.math.BigDecimal;

public class Fare {

    private int distance;
    private BigDecimal fare;
    private FareCalculator calculator;

    private Fare(FareCalculator calculator) {
        this.calculator = calculator;
    }

    private Fare(BigDecimal fare) {
        this.fare = fare;
    }

    public static Fare of(BigDecimal fare) {
        return new Fare(fare);
    }

    public static Fare of(FareCalculator calculator, int distance) {
        Fare fare = new Fare(calculator);
        fare.update(distance);

        return fare;
    }

    public void add(BigDecimal fare) {
        this.fare = this.fare.add(fare);
    }

    public void update(int distance) {
        this.distance = distance;
        this.fare = calculator.calculate(distance);
    }

    public BigDecimal getFare() {
        return this.fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fare fare1 = (Fare) o;

        return fare.equals(fare1.fare);
    }

    @Override
    public int hashCode() {
        return fare.hashCode();
    }
}
