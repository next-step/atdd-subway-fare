package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Fare;

public interface DiscountPolicy {
    Fare apply(Fare fare);
}
