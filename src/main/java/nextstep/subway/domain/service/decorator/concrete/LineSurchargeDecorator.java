package nextstep.subway.domain.service.decorator.concrete;


import nextstep.subway.domain.Line;
import nextstep.subway.domain.service.decorator.SurchargeDecorator;
import nextstep.subway.domain.service.decorator.component.SurchargeCalculator;

import java.util.List;


public class LineSurchargeDecorator extends SurchargeDecorator {

    private final List<Line> lines;

    public LineSurchargeDecorator(SurchargeCalculator surchargeCalculator, List<Line> lines) {
        super(surchargeCalculator);
        this.lines = lines;
    }

    @Override
    public int appendFare() {
        return extractSurcharge() + super.appendFare();
    }

    private int extractSurcharge() {
        return lines.stream()
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(0);
    }

}
