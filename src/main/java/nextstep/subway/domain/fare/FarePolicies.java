package nextstep.subway.domain.fare;

import java.util.ArrayList;
import java.util.List;

public class FarePolicies {

	private final List<FarePolicy> farePolicies = new ArrayList<>();

	public void addPolicy(FarePolicy farePolicy) {
		farePolicies.add(farePolicy);
	}

	public Fare calculateAll() {
		Fare fare = Fare.valueOf(0);
		for (FarePolicy farePolicy : farePolicies) {
			fare = farePolicy.calculate(fare);
		}

		return fare;
	}
}
