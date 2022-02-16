package nextstep.subway.domain;

import java.math.BigDecimal;

public class Fare {

    private int distance;
    private BigDecimal fare;
    private FareCalculator calculator;

    private Fare(FareCalculator calculator) {
        this.calculator = calculator;
    }

    public static Fare of(FareCalculator calculator, int distance) {
        Fare fare = new Fare(calculator);
        fare.update(distance);

        return fare;
    }

    public void update(int distance) {
        this.distance = distance;
        this.fare = calculator.calculate(distance);
    }

    public BigDecimal getFare() {
        return this.fare;
    }

}
