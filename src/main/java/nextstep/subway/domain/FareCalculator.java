package nextstep.subway.domain;

import java.util.function.Function;

public class FareCalculator {
	public static int calculate(int distance) {
		FarePolicy[] polices = FarePolicy.values();

		int totalFee = 0;
		for(FarePolicy policy: polices) {
			if(distance > policy.excessDistance) {
				totalFee += policy.getFare(distance - policy.excessDistance);
				distance = policy.excessDistance;
			}
		}

		return totalFee;
	}

	public static int calculate(Path path) {
		int totalFee = path.getSurcharge();
		int distance = path.extractDistance();

		FarePolicy[] polices = FarePolicy.values();
		for(FarePolicy policy: polices) {
			if(distance > policy.excessDistance) {
				totalFee += policy.getFare(distance - policy.excessDistance);
				distance = policy.excessDistance;
			}
		}

		return totalFee;
	}

	private enum FarePolicy {
		FEE50(50, distance -> calculate(distance, 8, 100)),
		FEE10(10, distance -> calculate(distance, 5, 100)),
		BASE(0, distance -> 1250)
		;

		private int excessDistance;
		private  Function<Integer, Integer> expression;

		FarePolicy(int excessDistance, Function<Integer, Integer> expression) {
			this.excessDistance = excessDistance;
			this.expression = expression;
		}

		public int getFare(int distance) {
			return expression.apply(distance);
		}

		private static int calculate(int distance, int unit, int unitFee) {
			return (int) ((Math.floor((distance - 1) / unit) + 1) * unitFee);
		}
	}
}
