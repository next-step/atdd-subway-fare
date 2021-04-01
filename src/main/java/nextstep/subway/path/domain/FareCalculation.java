package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

public interface FareCalculation {
    Fare calculate(Fare base, Distance distance);
}
