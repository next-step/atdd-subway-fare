package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Fare;

public interface Discount {
    Fare apply(Fare fare);
}
