package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;

public class LineSurchargeFareCalculator extends AbstractFareCalculatorChain {

    @Override
    public boolean support(Path path) {
        return true;
    }

    @Override
    protected int convert(Path path, int initialFare) {
        return initialFare + getMaximumSurcharge(path);
    }

    private int getMaximumSurcharge(Path path) {
        return path.getSections()
                .getSections()
                .stream()
                .map(Section::getLine)
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(0);
    }
}
