package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Fare;

public interface FarePolicy<T> {
	 Fare getFare(FarePolicyCondition<T> condition);
}
