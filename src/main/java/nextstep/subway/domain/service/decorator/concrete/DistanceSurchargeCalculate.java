package nextstep.subway.domain.service.decorator.concrete;

import lombok.AllArgsConstructor;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.service.decorator.component.SurchargeCalculator;

@AllArgsConstructor
public class DistanceSurchargeCalculate implements SurchargeCalculator {
    private final int distance;

    @Override
    public int appendFare() {
        return Fare.getFare(distance);
    }
}
