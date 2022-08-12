package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Fare {
	UNDER_10KM(Fare::isUnder10, Fare::calculatePrice),
	UNDER_50KM(Fare::isUnder50, Fare::calculateExcessFareUnder50),
	OVER_50KM(Fare::isOver50, Fare::calculateExcessFareOver50);

	private static final String FARE_NOT_FOUND = "요금을 찾을 수 없습니다.";
	public static final int BASIC_FARE = 1250;
	private static final int STANDARD = 10;
	private static final int FIFTY = 50;

	private Predicate<Integer> condition;
	private Function<Integer, Integer> calculate;

	Fare(Predicate<Integer> condition, Function<Integer, Integer> calculate) {
		this.condition = condition;
		this.calculate = calculate;
	}

	public static Integer calculate(int distance) {
		Fare fareType = Arrays.stream(values())
				.filter(fare -> fare.condition.test(distance))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(FARE_NOT_FOUND));

		return fareType.calculate.apply(distance);
	}

	private static boolean isUnder10(Integer distance) {
		return distance <= STANDARD;
	}

	private static boolean isUnder50(Integer distance) {
		return distance <= FIFTY;
	}

	private static boolean isOver50(Integer distance) {
		return distance > FIFTY;
	}

	private static int calculatePrice(int distance) {
		return BASIC_FARE;
	}

	private static int calculateExcessFareUnder50(int distance) {
		int overDistance = distance - STANDARD;
		return BASIC_FARE + (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
	}

	private static int calculateExcessFareOver50(int distance) {
		int overDistance = distance - FIFTY;
		return calculateExcessFareUnder50(FIFTY) + (int) ((Math.ceil((overDistance - 1) / 8) + 1) * 100);
	}
}
