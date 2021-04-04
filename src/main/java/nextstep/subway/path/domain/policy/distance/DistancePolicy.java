package nextstep.subway.path.domain.policy.distance;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.FarePolicyCondition;

public class DistancePolicy implements FarePolicy<Integer> {

	protected static final int DEFAULT_FARE = 1250;
	protected static final int FIRST_SECTION_LENGTH = 10;
	protected static final int FIRST_SURCHARGE_LENGTH = 5;
	protected static final int SECOND_SECTION_LENGTH = 50;
	protected static final int SECTION_SURCHARGE_LENGTH = 8;
	private static final int ZERO = 0;

	private static final List<FarePolicy<Integer>> POLICIES = Arrays.asList(
		new DefaultDistancePolicy(), new FirstOverDistancePolicy(), new SecondOverDistancePolicy());

	public Fare getFare(FarePolicyCondition<Integer> condition) {
		return POLICIES.stream()
			.map(distancePolicy -> distancePolicy.getFare(condition))
			.reduce(Fare::addFare)
			.orElse(Fare.of(ZERO));
	}

	private static int calculateOverFare(int distance, int sectionLength) {
		return (int) ((Math.ceil((distance - 1) / sectionLength) + 1) * 100);
	}
	private static class DefaultDistancePolicy implements FarePolicy<Integer> {

		@Override
		public Fare getFare(FarePolicyCondition<Integer> condition) {
			return Fare.of(DEFAULT_FARE);
		}
	}

	private static class FirstOverDistancePolicy implements FarePolicy<Integer> {
		@Override
		public Fare getFare(FarePolicyCondition<Integer> condition) {
			if (condition.getCondition() <= FIRST_SECTION_LENGTH) {
				return Fare.of(ZERO);
			}

			return Fare.of(
				calculateOverFare(getPolicyDistance(condition.getCondition()) - FIRST_SECTION_LENGTH, FIRST_SURCHARGE_LENGTH)
			);
		}

		private int getPolicyDistance(int distance) {
			return Math.min(distance, SECOND_SECTION_LENGTH);
		}
	}

	private static class SecondOverDistancePolicy implements FarePolicy<Integer> {
		@Override
		public Fare getFare(FarePolicyCondition<Integer> condition) {
			if (condition.getCondition() <= SECOND_SECTION_LENGTH) {
				return Fare.of(ZERO);
			}

			return Fare.of(
				calculateOverFare(condition.getCondition() - SECOND_SECTION_LENGTH, SECTION_SURCHARGE_LENGTH)
			);
		}
	}
}
