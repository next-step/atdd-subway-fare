package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.FarePolicyCondition;

public class LinePolicy implements FarePolicy<Integer> {

	@Override
	public Fare getFare(FarePolicyCondition<Integer> condition) {
		return Fare.of(condition.getCondition());
	}
}
