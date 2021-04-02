package nextstep.subway.path.domain;

import nextstep.subway.path.domain.specification.base.BaseFare;
import nextstep.subway.path.domain.specification.line.LineMaxFare;
import nextstep.subway.path.domain.specification.discount.AgeDiscount;
import nextstep.subway.path.domain.specification.distance.DistanceFare;


public class FareCalculatorFactory {
    public FareCalculatorFactory() { }

    public static FareCalculation getFareCalculator(FareParameter parameter){
        FareCalculator calculator = new FareCalculator(parameter);
        calculator.addBaseFarePolicy(new BaseFare());
        calculator.addLineFarePolicy(new LineMaxFare());
        calculator.addDistanceFarePolicy(new DistanceFare());
        calculator.addDiscountPolicy(AgeDiscount.of(parameter.getAge()));
        return calculator;
    }
}
