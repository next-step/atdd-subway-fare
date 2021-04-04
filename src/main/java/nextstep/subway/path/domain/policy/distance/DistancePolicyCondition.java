package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicyCondition;

public class DistancePolicyCondition implements FarePolicyCondition<Integer> {

	private final Integer distance;

	public DistancePolicyCondition(Integer distance) {
		this.distance = distance;
	}

	@Override
	public Integer getCondition() {
		return distance;
	}
}
