package nextstep.subway.domain.Fare.policy;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

public enum AgeFarePolicy {
	CHILDREN(age -> age > 5 && age < 13, fare -> (-1) * (fare - 350) / 2),
	TEENAGER(age -> age > 12 && age < 19, fare -> (-1) * (fare - 350) / 5),
	ADULT(age -> age > 19, fare -> 0);

	private final IntPredicate condition;
	private final IntFunction<Integer> calculate;

	AgeFarePolicy(IntPredicate condition, IntFunction<Integer> calculate) {
		this.condition = condition;
		this.calculate = calculate;
	}

	public static int calculateDiscountByAge(int age, int fare) {
		return Stream.of(AgeFarePolicy.values())
			.filter(farePolicy -> farePolicy.findPolicy(age))
			.findFirst()
			.map(farePolicy -> farePolicy.getFare(fare))
			.orElse(AgeFarePolicy.ADULT.getFare(fare));
	}

	private boolean findPolicy(int age) {
		return condition.test(age);
	}

	private int getFare(int fare) {
		return calculate.apply(fare);
	}
}
