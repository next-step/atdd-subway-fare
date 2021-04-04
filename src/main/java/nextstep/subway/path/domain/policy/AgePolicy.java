package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Fare;

public class AgePolicy implements FarePolicy {

	private static final int ZERO = 0;
	private static final int BABY_AGE = 6;
	private static final int CHILD_AGE = 13;
	private static final int YOUTH_AGE = 19;
	private static final double CHILD_DISCOUNT_RATE = 0.5;
	private static final double YOUTH_DISCOUNT_RATE = 0.2;

	private final FarePolicy farePolicy;

	private AgePolicy(FarePolicy farePolicy) {
		this.farePolicy = farePolicy;
	}

	public static AgePolicy of(int age) {
		if (age < BABY_AGE) {
			return new AgePolicy(new BabyAgePolicy());
		}

		if (age < CHILD_AGE) {
			return new AgePolicy(new ChildrenAgePolicy());
		}

		if (age < YOUTH_AGE) {
			return new AgePolicy(new YouthAgePolicy());
		}

		return new AgePolicy(new DefaultAgePolicy());
	}

	@Override
	public Fare getFare(int fare) {
		return farePolicy.getFare(fare);
	}

	private static int calculate(int fare, double discountRate) {
		return (int)(fare - Math.ceil((fare - 350) * discountRate));
	}

	private static class BabyAgePolicy implements FarePolicy {
		@Override
		public Fare getFare(int fare) {
			return Fare.of(ZERO);
		}
	}
	private static class ChildrenAgePolicy implements FarePolicy {
		@Override
		public Fare getFare(int fare) {
			return Fare.of(calculate(fare, CHILD_DISCOUNT_RATE));
		}
	}

	private static class YouthAgePolicy implements FarePolicy {
		@Override
		public Fare getFare(int fare) {
			return Fare.of(calculate(fare, YOUTH_DISCOUNT_RATE));
		}
	}

	private static class DefaultAgePolicy implements FarePolicy {
		@Override
		public Fare getFare(int fare) {
			return Fare.of(fare);
		}
	}

}
