package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum Discount {

	TEENAGER(13, 19, (amount) -> (int) ((amount - 350) * 0.8)),
	CHILD(6, 13, (amount) -> (int) ((amount - 350) * 0.5)),
	ADULT(14, Integer.MAX_VALUE, (amount) -> amount),
	NONE(0, 0, (amount) -> amount);

	private final int minAgeIncluded;
	private final int maxAgeExcluded;
	private final Function<Integer, Integer> calculator;

	Discount(int minAgeIncluded, int maxAgeExcluded, Function<Integer, Integer> calculator) {
		this.minAgeIncluded = minAgeIncluded;
		this.maxAgeExcluded = maxAgeExcluded;
		this.calculator = calculator;
	}

	public static Discount valueOf(int age) {
		return Arrays.stream(Discount.values())
				.filter(discount -> discount.minAgeIncluded <= age && age < discount.maxAgeExcluded)
				.findFirst()
				.orElse(NONE);
	}

	public static int calculateDiscountAmount(int fare, Integer age) {
		if (age == null || age <= 0) {
			return fare;
		}
		Discount discount = valueOf(age);
		return discount.calculator.apply(fare);
	}
}
