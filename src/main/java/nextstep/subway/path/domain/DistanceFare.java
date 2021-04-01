package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

public interface DistanceFare {
    static final int BASE_FARE = 1250;
    Fare calculate(Distance distance);
}
