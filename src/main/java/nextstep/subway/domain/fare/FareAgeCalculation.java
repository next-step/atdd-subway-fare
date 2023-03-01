package nextstep.subway.domain.fare;

import static nextstep.common.ErrorMsg.*;

public enum FareAgeCalculation {

	DEFAULT_DISCOUNT_FARE(350),
	KIDS_DISCOUNT_RATE(50),
	YOUTH_DISCOUNT_RATE(20),
	;

	private int value;

	FareAgeCalculation(int value) {
		this.value = value;
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

	private int isValue() {
		return value;
	}
}
