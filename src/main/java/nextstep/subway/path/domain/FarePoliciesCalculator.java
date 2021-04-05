package nextstep.subway.path.domain;

import java.util.Optional;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.policy.AgePolicy;
import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.FarePolicies;
import nextstep.subway.path.domain.policy.LinePolicy;

public class FarePoliciesCalculator {
	public static Fare calculate(PathResult pathResult,
		LoginMember loginMember) {
		FarePolicies farePolicies = FarePolicies.of(
			LinePolicy.of(pathResult.getHighCharge()),
			DistancePolicy.of(pathResult.getTotalDistance()),
			AgePolicy.of(loginMember.getAge())
		);

		return farePolicies.calculate();
	}
}
