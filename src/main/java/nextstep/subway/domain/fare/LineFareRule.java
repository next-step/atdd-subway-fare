package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import org.springframework.util.ObjectUtils;

public class LineFareRule implements FareCalculationRule {
    private FareCalculationRule nextRule;

    public void setNextRule(FareCalculationRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public int calculateFare(Path path, int age, int fare) {
        int surcharge = calculateFare(path.getSections());
        fare += surcharge;

        if (ObjectUtils.isEmpty(nextRule)) {
            return fare;
        } else {
            return nextRule.calculateFare(path, age, fare);
        }
    }

    private int calculateFare(Sections sections) {
        return sections.getSections().stream()
                .map(Section::getLine)
                .map(Line::getSurcharge)
                .reduce(Integer::max)
                .orElse(0);
    }
}
