package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Fare;

public class LinePolicy implements FarePolicy {

	private final int charge;

	private LinePolicy(int charge) {
		this.charge = charge;
	}

	public static FarePolicy of(int charge) {
		return new LinePolicy(charge);
	}

	@Override
	public Fare getFare() {
		return Fare.of(charge);
	}
}
