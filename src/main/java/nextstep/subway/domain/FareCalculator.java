package nextstep.subway.domain;

public class FareCalculator {

	private static final int STANDARD_FARE_AMOUNT = 1250;
	private static final int STANDARD_FARE_DISTANCE = 10;
	private static final int EXTRA_FARE_DISTANCE = 50;

	public static int calculateFare(int distance) {
		if (distance <= 0) {
			return 0;
		}
		if (distance <= STANDARD_FARE_DISTANCE) {
			return STANDARD_FARE_AMOUNT;
		}
		if (distance <= EXTRA_FARE_DISTANCE) {
			return STANDARD_FARE_AMOUNT
					+ calculateExtraFareLessThanOrEqualTo50Km(distance - STANDARD_FARE_DISTANCE);
		}
		return STANDARD_FARE_AMOUNT
				+ calculateExtraFareLessThanOrEqualTo50Km(EXTRA_FARE_DISTANCE - STANDARD_FARE_DISTANCE)
				+ calculateExtraFareOver50Km(distance - EXTRA_FARE_DISTANCE);
	}

	private static int calculateExtraFareLessThanOrEqualTo50Km(int distance) {
		if (distance <= 0) {
			return 0;
		}
		return ((int) (Math.ceil(distance - 1) / 5) + 1) * 100;
	}

	private static int calculateExtraFareOver50Km(int distance) {
		if (distance <= 0) {
			return 0;
		}
		return ((int) (Math.ceil(distance - 1) / 8) + 1) * 100;
	}
}
