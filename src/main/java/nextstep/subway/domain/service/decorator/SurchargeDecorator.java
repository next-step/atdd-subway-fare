package nextstep.subway.domain.service.decorator;

import lombok.AllArgsConstructor;
import nextstep.subway.domain.service.decorator.component.SurchargeCalculator;

@AllArgsConstructor
public abstract class SurchargeDecorator implements SurchargeCalculator {
    private final SurchargeCalculator surchargeCalculator;

    @Override
    public int appendFare() {
        return surchargeCalculator.appendFare();
    }
}
