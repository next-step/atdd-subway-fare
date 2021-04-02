package nextstep.subway.path.domain.specification.base;

import nextstep.subway.path.domain.BaseFarePolicy;
import nextstep.subway.path.domain.valueobject.Fare;

public class BaseFare implements BaseFarePolicy {
    private static Fare BASE_FARE = Fare.of(1250);

    @Override
    public Fare calculate() {
        return BASE_FARE;
    }
}
