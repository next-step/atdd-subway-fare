package nextstep.subway.domain.fare;

public class LineFarePolicy implements FarePolicy {

	private final int maxFareOnLine;

	public LineFarePolicy(int maxFareOnLine) {
		this.maxFareOnLine = maxFareOnLine;
	}

	@Override
	public Fare calculate(Fare fare) {
		return fare.plus(Fare.valueOf(maxFareOnLine));
	}
}
