package nextstep.api.subway.util;

/**
 * @author : Rene Choi
 * @since : 2024/02/25
 */
public class FareCalculator {
	private static final int BASIC_FARE = 1250;
	private static final int FIRST_THRESHOLD = 10; // km
	private static final int SECOND_THRESHOLD = 50; // km
	private static final int FIRST_INCREMENT = 5; // km
	private static final int SECOND_INCREMENT = 8; // km
	private static final int ADDITIONAL_FARE = 100; // won

	public static int calculateFare(long distance) {
		if (distance <= FIRST_THRESHOLD) {
			return BASIC_FARE;
		}
		if (distance <= SECOND_THRESHOLD) {
			return BASIC_FARE + calculateIncrementalFare(distance - FIRST_THRESHOLD, FIRST_INCREMENT);
		}
		return BASIC_FARE
			+ calculateIncrementalFare(SECOND_THRESHOLD - FIRST_THRESHOLD, FIRST_INCREMENT)
			+ calculateIncrementalFare(distance - SECOND_THRESHOLD, SECOND_INCREMENT);
	}

	private static int calculateIncrementalFare(long distance, int increment) {
		return (int) ((Math.ceil((double) distance / increment)) * ADDITIONAL_FARE);
	}
}
