package nextstep.subway.maps.fare.domain.policy;

import nextstep.subway.maps.fare.domain.FareContext;

public interface FarePolicy {
    void calculate(FareContext fareContext);
}
