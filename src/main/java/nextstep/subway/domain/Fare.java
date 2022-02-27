package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum Fare {
	BASE(0, 10, distance -> 1250),
	FARE10(10, 50, distance -> 1250 + (int) ((Math.floor((distance - 11) / 5) + 1) * 100)),
	FARE50(50, Integer.MAX_VALUE, distance -> 1250 + (int) ((Math.floor((50 - 11) / 5) + 1) * 100)
																+ (int) ((Math.floor((distance - 51) / 8) + 1) * 100))
	;


	private int excessDistance;
	private int belowDistance;
	private final Function<Integer, Integer> expression;

	Fare(int excessDistance, int belowDistance, Function<Integer, Integer> expression) {
		this.excessDistance = excessDistance;
		this.belowDistance = belowDistance;
		this.expression = expression;
	}

	public int calculate(int distance) {
		return expression.apply(distance);
	}

	public static Fare findFare(int distance) {
		return Arrays.stream(Fare.values())
			.filter(it -> (it.excessDistance < distance && distance<= it.belowDistance))
			.findFirst()
			.orElse(BASE);
	}
}
