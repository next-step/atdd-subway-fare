package nextstep.subway.path.domain;

public class Fare {

	private final int fare;

	private Fare(int fare) {
		this.fare = fare;
	}

	public static Fare of(int fare) {
		return new Fare(fare);
	}
	public int getFare() {
		return fare;
	}

	public Fare addFare(Fare addFare) {
		return new Fare(this.fare + addFare.fare);
	}
}
