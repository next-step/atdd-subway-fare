package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;

public interface FarePolicy {
   Fare getFare(Fare fare);
}
