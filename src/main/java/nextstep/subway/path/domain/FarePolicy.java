package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;

public interface FarePolicy {
   int calculate(FarePolicyRequest request);
}
