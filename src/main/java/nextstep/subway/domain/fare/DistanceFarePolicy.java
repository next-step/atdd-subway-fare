package nextstep.subway.domain.fare;

import java.util.Arrays;

public class DistanceFarePolicy implements FarePolicy {

	private final int distance;
	private final DistanceFare distanceFareCalculator;

	public DistanceFarePolicy(int distance) {
		this.distance = distance;
		distanceFareCalculator = DistanceFare.valueOfDistance(distance);
	}

	enum DistanceFare {
		STANDARD(null, 1250, 1, 11, 10),
		EXTRA_LESS_THAN_50KM(STANDARD, 100, 11, 51, 5),
		EXTRA_OVER_50KM(EXTRA_LESS_THAN_50KM, 100, 51, Integer.MAX_VALUE, 8),
		;

		final int amount;
		final int minDistanceIncluded;
		final int maxDistanceExcluded;
		final int maxDistance;
		final DistanceFare parentFare;
		final int dividend;

		DistanceFare(DistanceFare parentFare, int amount, int minDistanceIncluded, int maxDistanceExcluded, int dividend) {
			this.parentFare = parentFare;
			this.amount = amount;
			this.minDistanceIncluded = minDistanceIncluded;
			this.maxDistanceExcluded = maxDistanceExcluded;
			this.maxDistance = maxDistanceExcluded - 1;
			this.dividend = dividend;
		}

		private static DistanceFare valueOfDistance(int distance) {
			return Arrays.stream(DistanceFare.values())
					.filter(fare -> fare.isWithinDistance(distance))
					.findFirst()
					.orElseThrow(IllegalArgumentException::new);
		}

		private boolean isWithinDistance(int distance) {
			return minDistanceIncluded <= distance && distance < maxDistanceExcluded;
		}

		private int calculate(int distance) {
			if (distance <= 0) {
				return 0;
			}
			if (parentFare == null) {
				return amount;
			}
			return parentFare.calculate(parentFare.maxDistance)
					+ ((int) (Math.ceil(distance - (minDistanceIncluded - 1) - 1) / dividend) + 1) * amount;
		}
	}

	@Override
	public Fare calculate(Fare fare) {
		return fare.plus(Fare.valueOf(distanceFareCalculator.calculate(distance)));
	}
}
