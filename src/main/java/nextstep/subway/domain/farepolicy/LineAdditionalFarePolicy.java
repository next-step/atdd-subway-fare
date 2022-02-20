package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.map.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.farepolicy.base.FarePolicy;

public class LineAdditionalFarePolicy implements FarePolicy {
    @Override
    public int calculate(Path path) {
        return path.getSections()
                   .getSections().stream()
                   .mapToInt(Section::getAdditionalFare)
                   .max().orElse(0);
    }
}
