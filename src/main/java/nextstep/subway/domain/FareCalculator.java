package nextstep.subway.domain;

import nextstep.subway.domain.fare.DefaultFareCalculator;
import nextstep.subway.domain.fare.FareCalculatorChain;
import nextstep.subway.domain.fare.FirstRangeFareCalculator;
import nextstep.subway.domain.fare.SecondRangeFareCalculator;

public class FareCalculator {

    private final FareCalculatorChain calculatorChain;

    public FareCalculator() {
        var defaultFareCalculator = new DefaultFareCalculator();
        var firstFareCalculator = new FirstRangeFareCalculator();
        var secondFareCalculator = new SecondRangeFareCalculator();

        defaultFareCalculator.setNextChain(firstFareCalculator);
        firstFareCalculator.setNextChain(secondFareCalculator);

        this.calculatorChain = defaultFareCalculator;
    }

    public int getFare(Path path) {
        return calculatorChain.calculate(path, 0);
    }

}
