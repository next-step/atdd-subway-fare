package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.FarePolicies;
import nextstep.subway.path.domain.policy.LinePolicy;

public class FarePoliciesCalculator {
	public static Fare calculate(PathResult pathResult) {
		FarePolicies farePolicies = FarePolicies.of(
			LinePolicy.of(pathResult.getHighCharge()),
			DistancePolicy.of(pathResult.getTotalDistance())
		);

		return farePolicies.calculate();
	}
}
