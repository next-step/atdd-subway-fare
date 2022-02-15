package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;

public class LineAdditionalFarePolicy implements FarePolicy {
    @Override
    public int calculate(Path path) {
        return path.getSections()
                   .getSections().stream()
                   .mapToInt(Section::getAdditionalFare)
                   .max().orElse(0);
    }
}
