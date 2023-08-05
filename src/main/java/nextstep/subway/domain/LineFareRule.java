package nextstep.subway.domain;

import nextstep.member.domain.Member;
import org.springframework.util.ObjectUtils;

public class LineFareRule implements FareCalculationRule {

    private FareCalculationRule nextRule;

    @Override
    public int calculateFare(Path path, Member member, int totalFare) {
        int surcharge = path.getSections().getSections().stream()
                .map(Section::getLine)
                .map(Line::getSurcharge)
                .reduce(Integer::max)
                .orElse(0);

        if (ObjectUtils.isEmpty(nextRule)) {
            return totalFare + surcharge;
        } else {
            return nextRule.calculateFare(path, member, totalFare + surcharge);
        }
    }

    public void setNextRule(FareCalculationRule nextRule) {
        this.nextRule = nextRule;
    }
}
