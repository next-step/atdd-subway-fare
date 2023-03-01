package nextstep.subway.domain;

import static nextstep.common.ErrorMsg.*;

public enum FareCalculation {

	DEFAULT_DISTANCE(10),
	LONG_DISTANCE(50),

	UNDER_LONG_DISTANCE(5),
	OVER_LONG_DISTANCE(8),

	DEFAULT_FARE(1250),
	UNDER_LONG_FARE(800),
	ADD_FARE(100),

	DEFAULT_DISCOUNT_FARE(350),
	KIDS_DISCOUNT_RATE(50),
	YOUTH_DISCOUNT_RATE(20),
	;

	private int value;

	FareCalculation(int value) {
		this.value = value;
	}

	public static int fareDistanceCalculation(int distance) {
		if (distance <= 0) {
			throw new IllegalArgumentException(PATH_DISTANCE_0_UNDER_VALUE.isMessage());
		}
		if (distance <= DEFAULT_DISTANCE.isValue()) {
			return DEFAULT_FARE.isValue();
		}
		if (distance > DEFAULT_DISTANCE.isValue() && distance <= LONG_DISTANCE.isValue()) {
			distance -= DEFAULT_DISTANCE.isValue();
			return DEFAULT_FARE.isValue() + calculateOverFare(distance, UNDER_LONG_DISTANCE.isValue());
		}
		if (distance > LONG_DISTANCE.isValue()) {
			distance -= LONG_DISTANCE.isValue();
			return DEFAULT_FARE.isValue() + UNDER_LONG_FARE.isValue() + calculateOverFare(distance, OVER_LONG_DISTANCE.isValue());
		}
		throw new IllegalArgumentException(PATH_DISTANCE_WRONG_VALUE.isMessage());
	}

	public static int fareAgeCalculation(int fare, int age) {
		if (age <= 0) {
			throw new IllegalArgumentException(MEMBER_AGE_WRONG_VALUE.isMessage());
		}
		if (age >= 19) {
			return fare;
		}
		fare -= DEFAULT_DISCOUNT_FARE.isValue();
		if (age >= 6 && age < 13) {
			return (fare * (100 - KIDS_DISCOUNT_RATE.isValue())) / 100;
		}
		if (age >= 13 && age < 19) {
			return (fare * (100 - YOUTH_DISCOUNT_RATE.isValue())) / 100;
		}
		throw new IllegalArgumentException(MEMBER_AGE_WRONG_VALUE.isMessage());
	}

	private static int calculateOverFare(int overDistance, int farePerDistance) {
		return (int)((Math.ceil((overDistance - 1) / farePerDistance) + 1) * ADD_FARE.isValue());
	}

	private int isValue() {
		return value;
	}
}
