package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Fare;

public interface FarePolicy {
    Fare calculate(Fare input);
}
