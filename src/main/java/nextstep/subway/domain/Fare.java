package nextstep.subway.domain;

import java.util.Arrays;

public enum Fare {
	STANDARD(null, 1250, 1, 11, 10),
	EXTRA_LESS_THAN_50KM(STANDARD, 100, 11, 51, 5),
	EXTRA_OVER_50KM(EXTRA_LESS_THAN_50KM, 100, 51, Integer.MAX_VALUE, 8),
	;

	private final Fare parentFare;
	private final int amount;
	private final int minDistanceIncluded;
	private final int maxDistanceExcluded;
	private final int dividend;

	Fare(Fare parentFare, int amount, int minDistanceIncluded, int maxDistanceExcluded, int dividend) {
		this.parentFare = parentFare;
		this.amount = amount;
		this.minDistanceIncluded = minDistanceIncluded;
		this.maxDistanceExcluded = maxDistanceExcluded;
		this.dividend = dividend;
	}

	public static int calculateAmount(Path path, Integer age) {
		int fare = Fare.calculateAmount(path) + path.getExtraFareOnLine();
		return Discount.calculateDiscountAmount(fare, age);
	}

	public static int calculateAmount(Path path) {
		int distance = path.extractDistance();
		Fare fare = valueOfDistance(distance);
		return fare.calculate(distance);
	}

	public static int calculateAmount(int distance) {
		Fare fare = valueOfDistance(distance);
		return fare.calculate(distance);
	}

	public static Fare valueOfDistance(int distance) {
		return Arrays.stream(Fare.values())
				.filter(fare -> fare.minDistanceIncluded <= distance && distance < fare.maxDistanceExcluded)
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	private int calculate(int distance) {
		if (distance <= 0) {
			return 0;
		}
		if (parentFare == null) {
			return amount;
		}
		return parentFare.calculate(parentFare.maxDistanceExcluded - 1)
				+ ((int) (Math.ceil(distance - (minDistanceIncluded - 1) - 1) / dividend) + 1) * amount;
	}
}
