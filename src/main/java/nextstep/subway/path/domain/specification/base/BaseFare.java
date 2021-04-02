package nextstep.subway.path.domain.specification.base;

import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.valueobject.Fare;

public class BaseFare implements FarePolicy {
    private static Fare BASE_FARE = Fare.of(1250);

    @Override
    public Fare calculate(Fare input) {
        return BASE_FARE;
    }
}
