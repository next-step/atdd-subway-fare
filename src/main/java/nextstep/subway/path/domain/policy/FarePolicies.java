package nextstep.subway.path.domain.policy;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.path.domain.Fare;

public class FarePolicies {

	private static final int ZERO = 0;

	private final List<FarePolicy> farePolicies;

	private FarePolicies(List<FarePolicy> farePolicies) {
		this.farePolicies = farePolicies;
	}

	public static FarePolicies of(FarePolicy... policies) {
		return new FarePolicies(Arrays.asList(policies));
	}

	public Fare calculate() {
		Fare fare = Fare.of(ZERO);
		for (FarePolicy farePolicy : farePolicies) {
			fare = farePolicy.getFare(fare.getFare());
		}
		return fare;
	}
}
