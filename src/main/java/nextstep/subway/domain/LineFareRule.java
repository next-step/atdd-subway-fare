package nextstep.subway.domain;

import nextstep.member.domain.Member;
import org.springframework.util.ObjectUtils;

public class LineFareRule implements FareCalculationRule {
    private FareCalculationRule nextRule;

    public void setNextRule(FareCalculationRule nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public int calculateFare(Path path, Member member, int fare) {
        int surcharge = calculateFare(path.getSections());
        fare += surcharge;

        if (ObjectUtils.isEmpty(nextRule)) {
            return fare;
        } else {
            return nextRule.calculateFare(path, member, fare);
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
