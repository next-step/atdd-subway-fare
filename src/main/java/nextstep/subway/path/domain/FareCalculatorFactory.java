package nextstep.subway.path.domain;

import nextstep.subway.path.domain.specification.discount.AgeDiscount;
import nextstep.subway.path.domain.specification.distance.FirstDistanceFare;


public class FareCalculatorFactory {
    public FareCalculatorFactory() { }

    public static FareCalculation getFareCalculator(FareSpecification specification){
        FareCalculator calculator = new FareCalculator();
        calculator.setBaseFareStrategy(new FirstDistanceFare());
        if (specification.hasAge()) {
            calculator.setDiscountFareStrategy(AgeDiscount.of(specification.getAge()));
        }
        return calculator;
    }
}
