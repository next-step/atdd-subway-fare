package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;

public class AgeDiscountFarePolicy implements FarePolicy {

	private final AgeDiscountFareCalculator calculator;

	public AgeDiscountFarePolicy(int age) {
		this.calculator = AgeDiscountFareCalculator.valueOf(age);
	}


	@Override
	public Fare calculate(Fare fare) {
		return Fare.valueOf(calculator.calculate(fare.getAmount()));
	}

	enum AgeDiscountFareCalculator {

		TEENAGER(13, 19, (amount) -> (int) ((amount - 350) * 0.8)),
		CHILD(6, 13, (amount) -> (int) ((amount - 350) * 0.5)),
		ADULT(14, Integer.MAX_VALUE, (amount) -> amount),
		NONE(0, 0, (amount) -> amount);

		private final int minAgeIncluded;
		private final int maxAgeExcluded;
		private final Function<Integer, Integer> calculator;

		AgeDiscountFareCalculator(int minAgeIncluded, int maxAgeExcluded, Function<Integer, Integer> calculator) {
			this.minAgeIncluded = minAgeIncluded;
			this.maxAgeExcluded = maxAgeExcluded;
			this.calculator = calculator;
		}

		public static AgeDiscountFareCalculator valueOf(int age) {
			return Arrays.stream(AgeDiscountFareCalculator.values())
					.filter(discount -> discount.minAgeIncluded <= age && age < discount.maxAgeExcluded)
					.findFirst()
					.orElse(NONE);
		}

		public int calculate(int fare) {
			return calculator.apply(fare);
		}
	}

}
