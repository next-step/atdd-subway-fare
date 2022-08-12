package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountPolicy {
	CHILD(DiscountPolicy::isChild, DiscountPolicy::calculateChild),
	TEENAGER(DiscountPolicy::isTeenAger, DiscountPolicy::calculateTeenager),
	ADULT(DiscountPolicy::isAdult, DiscountPolicy::calculateAdult);

	public static final String POLICY_NOT_FOUND = "요금 정책을 찾을 수 없습니다.";
	private Predicate<Integer> condition;
	private Function<Integer, Integer> calculate;

	DiscountPolicy(Predicate<Integer> condition, Function<Integer, Integer> calculate) {
		this.condition = condition;
		this.calculate = calculate;
	}

	public static int calculate(int age, int fare) {
		DiscountPolicy policy = Arrays.stream(values())
				.filter(it -> it.condition.test(age))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(POLICY_NOT_FOUND));

		return policy.calculate.apply(fare);
	}

	private static boolean isChild(Integer age) {
		return age >= 6 && age < 13;
	}

	private static boolean isTeenAger(Integer age) {
		return age < 19;
	}

	private static boolean isAdult(Integer age) {
		return age >= 19;
	}

	private static Integer calculateChild(Integer fare) {
		return (int) ((fare - 350) * 0.5);
	}

	private static Integer calculateTeenager(Integer fare) {
		return (int) ((fare - 350) * 0.8);
	}

	private static Integer calculateAdult(Integer fare) {
		return fare;
	}
}
