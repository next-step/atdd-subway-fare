package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicyCondition;

public class LinePolicyCondition implements FarePolicyCondition<Integer> {

	private final Integer charge;

	public LinePolicyCondition(Integer charge) {
		this.charge = charge;
	}

	@Override
	public Integer getCondition() {
		return charge;
	}
}
