package nextstep.subway.domain;

import nextstep.subway.exception.NegativeDistanceException;

public class Fare {
	private int distance;

	private Fare(int distance) {
		this.distance = distance;
	}

	public static Fare of(int distance) {
		if (!isPositive(distance)) {
			throw new NegativeDistanceException();
		}

		return new Fare(distance);
	}

	private static boolean isPositive(int distance) {
		return distance > 0;
	}

	public int getFare() {
		return FarePolicy.calculateByDistance(distance);
	}
}
