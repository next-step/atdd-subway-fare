package nextstep.subway.path.domain.specification.line;

import nextstep.subway.path.domain.BaseFarePolicy;
import nextstep.subway.path.domain.LineFarePolicy;
import nextstep.subway.path.domain.valueobject.Fare;

public class LineMaxFare implements LineFarePolicy {
    @Override
    public Fare calculate(Fare fare) {
        return fare;
    }
}
