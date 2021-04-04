package nextstep.subway.path.domain.policy;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.path.domain.Fare;

public class DistancePolicy implements FarePolicy {

	protected static final int DEFAULT_FARE = 1250;
	protected static final int FIRST_SECTION_LENGTH = 10;
	protected static final int FIRST_SURCHARGE_LENGTH = 5;
	protected static final int SECOND_SECTION_LENGTH = 50;
	protected static final int SECTION_SURCHARGE_LENGTH = 8;
	private static final int ZERO = 0;

	private final List<FarePolicy> policies;

	private DistancePolicy(int distance) {
		policies = Arrays.asList(
			new DefaultDistancePolicy(distance),
			new FirstOverDistancePolicy(distance),
			new SecondOverDistancePolicy(distance)
		);
	}

	public static DistancePolicy of(int distance) {
		return new DistancePolicy(distance);
	}

	@Override
	public Fare getFare() {
		return policies.stream()
			.map(FarePolicy::getFare)
			.reduce(Fare::addFare)
			.orElse(Fare.of(ZERO));
	}

	private static int calculateOverFare(int distance, int sectionLength) {
		return (int) ((Math.ceil((distance - 1) / sectionLength) + 1) * 100);
	}
	private static class DefaultDistancePolicy implements FarePolicy {
		private int distance;
		public DefaultDistancePolicy(int distance) {
			this.distance = distance;
		}

		@Override
		public Fare getFare() {
			return Fare.of(DEFAULT_FARE);
		}
	}

	private static class FirstOverDistancePolicy implements FarePolicy {
		private int distance;
		public FirstOverDistancePolicy(int distance) {
			this.distance = distance;
		}
		@Override
		public Fare getFare() {
			if (distance <= FIRST_SECTION_LENGTH) {
				return Fare.of(ZERO);
			}

			return Fare.of(
				calculateOverFare(getPolicyDistance(distance) - FIRST_SECTION_LENGTH, FIRST_SURCHARGE_LENGTH)
			);
		}

		private int getPolicyDistance(int distance) {
			return Math.min(distance, SECOND_SECTION_LENGTH);
		}
	}

	private static class SecondOverDistancePolicy implements FarePolicy {
		private int distance;
		public SecondOverDistancePolicy(int distance) {
			this.distance = distance;
		}

		@Override
		public Fare getFare() {
			if (distance <= SECOND_SECTION_LENGTH) {
				return Fare.of(ZERO);
			}

			return Fare.of(
				calculateOverFare(distance - SECOND_SECTION_LENGTH, SECTION_SURCHARGE_LENGTH)
			);
		}
	}
}
