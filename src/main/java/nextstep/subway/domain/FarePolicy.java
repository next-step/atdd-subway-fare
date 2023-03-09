package nextstep.subway.domain;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

public enum FarePolicy {
	BASIC(distance -> distance > 0,
		distance -> 1_250),
	OVER_10KM_AND_UNDER_50KM(distance -> distance > 10,
		distance -> (distance - 10 / 5) * 100),
	OVER_50KM(distance -> distance > 50,
		distance -> (distance - 50 / 8) * 100);

	private final IntPredicate condition;
	private final IntFunction<Integer> calculate;

	FarePolicy(IntPredicate condition, IntFunction<Integer> calculate) {
		this.condition = condition;
		this.calculate = calculate;
	}

	public static int calculateByDistance(int distance) {
		return Stream.of(FarePolicy.values())
			.filter(farePolicy -> farePolicy.findPolicy(distance))
			.mapToInt(farePolicy -> farePolicy.getFare(distance))
			.sum();
	}

	private boolean findPolicy(int distance) {
		return condition.test(distance);
	}

	private int getFare(int distance) {
		return calculate.apply(distance);
	}
}
