package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public interface FarePolicy {

    Fare apply(Fare fare, FareParams fareParams);

}
