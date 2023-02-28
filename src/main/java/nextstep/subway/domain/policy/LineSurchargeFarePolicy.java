package nextstep.subway.domain.policy;

import nextstep.subway.domain.Line;

public class LineSurchargeFarePolicy implements FarePolicy{

    @Override
    public int calculate(CalculateConditions conditions) {
        return conditions.getLines().stream()
                .map(Line::getSurcharge)
                .max(Integer::compareTo)
                .orElse(0);
    }
}
