package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Fare;

public class LinePolicy implements FarePolicy {

	private final int charge;

	private LinePolicy(int charge) {
		this.charge = charge;
	}

	public static LinePolicy of(int charge) {
		return new LinePolicy(charge);
	}

	@Override
	public Fare getFare(int fare) {
		return Fare.of(charge + fare);
	}
}
