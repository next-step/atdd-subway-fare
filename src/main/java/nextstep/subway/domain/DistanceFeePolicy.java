package nextstep.subway.domain;

public class DistanceFeePolicy implements FeePolicy {

	private static final long BASIC_FEE = 1250;
	private static final int BASE_DISTANCE = 10;
	private static final int DIVISION_ONE_DISTANCE = 50;

	@Override
	public long calculateFee(int totalDistance) {
		if (totalDistance < BASE_DISTANCE) {
			return BASIC_FEE;
		}
		return 0;
	}

	private long getDivisionOneDistance(int totalDistance) {
		// 50 -20 = 30
		if (DIVISION_ONE_DISTANCE - totalDistance > 0) {

		}
		return 0;
	}

	private long getDivisionTwoDistance(int totalDistance) {
		return 0;
	}

	private int calculateOverFare(int distance) {
		return (int)((Math.ceil((distance - 1) / 5) + 1) * 100);
	}
}
