package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import org.springframework.stereotype.Component;

@Component
public class LineSurchargeFarePolicy implements FarePolicy {

    @Override
    public int fare(Path path) {
        return getMaximumSurcharge(path);
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
