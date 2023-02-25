package nextstep.subway.domain;

public class FareCalculation {

	private static final int DEFAULT_DISTANCE = 10;
	private static final int LONG_DISTANCE = 50;

	private static final int UNDER_LONG_DISTANCE = 5;
	private static final int OVER_LONG_DISTANCE = 8;

	private static final int DEFAULT_FARE = 1250;
	private static final int ADD_FARE = 100;

	public static int fareCalculation(int distance) {
		if (distance <= DEFAULT_DISTANCE && distance > 0) {
			return DEFAULT_FARE;
		}
		if (distance > DEFAULT_DISTANCE && distance <= LONG_DISTANCE) {
			return DEFAULT_FARE + calculateOverFare(distance, UNDER_LONG_DISTANCE);
		}
		if (distance > LONG_DISTANCE ) {
			return DEFAULT_FARE + calculateOverFare(distance, OVER_LONG_DISTANCE);
		}
		throw new IllegalArgumentException();
	}

	private static int calculateOverFare(int overDistance, int farePerDistance) {
		return (int) ((Math.ceil((overDistance -DEFAULT_DISTANCE - 1) / farePerDistance) + 1) * ADD_FARE);
	}
}
