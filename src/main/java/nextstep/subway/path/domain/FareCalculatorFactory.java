package nextstep.subway.path.domain;

import nextstep.subway.path.domain.specification.base.BaseFare;
import nextstep.subway.path.domain.specification.line.LineMaxFare;
import nextstep.subway.path.domain.specification.discount.AgeDiscountFactory;
import nextstep.subway.path.domain.specification.distance.DistanceFare;


public class FareCalculatorFactory {
    public FareCalculatorFactory() { }

    public static FareCalculation getFareCalculator(FareParameter parameter){
        FareCalculator calculator = new FareCalculator(parameter);
        calculator.addAllBaseFarePolicy(
                new BaseFare(),
                new LineMaxFare(parameter.getLineMaxFare()),
                new DistanceFare(parameter.getDistance()),
                AgeDiscountFactory.getDiscount(parameter.getAge()));
        return calculator;
    }
}
