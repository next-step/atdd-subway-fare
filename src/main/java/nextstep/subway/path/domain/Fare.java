package nextstep.subway.path.domain;

public class Fare {

	private static final int DEFAULT_FARE = 1250;
	private static final int FIRST_SECTION_LENGTH = 10;
	private static final int SECOND_SECTION_LENGTH = 50;
	private static final int FIRST_SURCHARGE_LENGTH = 5;
	private static final int SECTION_SURCHARGE_LENGTH = 8;

	private final int fare;

	private Fare(int distance) {
		this.fare = calculateFare(distance);
	}

	public static Fare of(int distance) {
		return new Fare(distance);
	}
	private int calculateFare(int distance) {
		if (distance <= FIRST_SECTION_LENGTH) {
			return DEFAULT_FARE;
		}
		if (distance <= SECOND_SECTION_LENGTH) {
			return DEFAULT_FARE + calculateOverFare(distance - FIRST_SECTION_LENGTH, FIRST_SURCHARGE_LENGTH);
		}
		return DEFAULT_FARE + calculateOverFare(firstSectionCalculate(distance), FIRST_SURCHARGE_LENGTH) +
			calculateOverFare(distance - SECOND_SECTION_LENGTH, SECTION_SURCHARGE_LENGTH);
	}

	private int firstSectionCalculate(int distance) {
		return distance - (distance % SECOND_SECTION_LENGTH) - FIRST_SECTION_LENGTH;
	}

	private int calculateOverFare(int distance, int sectionLength) {
		return (int) ((Math.ceil((distance - 1) / sectionLength) + 1) * 100);
	}

	public int getFare() {
		return fare;
	}
}
