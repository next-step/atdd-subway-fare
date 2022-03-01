package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public class AgeDiscountCalculator {
	public static int calculate(int fare, int age) {
		return AgePolicy.findPolicyByAge(age).getDiscountFare(fare);
	}

	private enum AgePolicy {
		NONE(0, 6, fare -> fare),
		CHILDREN(6, 13, fare -> calculate(fare, 0.5)),
		YOUTH(13, 19, fare -> calculate(fare, 0.2)),
		ADULT(19, Integer.MAX_VALUE, fare -> fare)
		;

		private int minAge;
		private int maxAge;
		private Function<Integer, Integer> expression;

		AgePolicy(int minAge, int maxAge, Function<Integer, Integer> expression) {
			this.minAge = minAge;
			this.maxAge = maxAge;
			this.expression = expression;
		}

		public static AgePolicy findPolicyByAge(int age) {
			return Arrays.stream(AgePolicy.values())
				.filter(it -> it.minAge <= age && age < it.maxAge)
				.findFirst()
				.orElse(NONE);
		}

		public int getDiscountFare(int fare) {
			return expression.apply(fare);
		}

		private static int calculate(int fare, double discountRate) {
			return (int)((fare - 350) * (1 - discountRate));
		}
	}
}
