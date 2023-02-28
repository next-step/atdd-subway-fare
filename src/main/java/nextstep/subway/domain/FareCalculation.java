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
	;

	private int value;

	FareCalculation(int value) {
		this.value = value;
	}

	public static int fareCalculation(int distance) {
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

	private static int calculateOverFare(int overDistance, int farePerDistance) {
		return (int)((Math.ceil((overDistance - 1) / farePerDistance) + 1) * ADD_FARE.isValue());
	}

	private int isValue() {
		return value;
	}
}
