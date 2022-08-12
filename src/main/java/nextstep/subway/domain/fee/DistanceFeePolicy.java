package nextstep.subway.domain.fee;

public class DistanceFeePolicy implements FeePolicy {

	private static final long BASIC_FEE = 1250;
	private static final int BASE_DISTANCE = 10;
	private static final int DIVISION_ONE_DISTANCE = 50;
	private static final int DIVISION_ONE_BASE_DISTANCE = 5;
	private static final int OVER_DIVISION_ONE_BASE_DISTANCE = 8;

	private int totalDistance;

	public DistanceFeePolicy(int totalDistance) {
		if (totalDistance <= 0) {
			throw new IllegalArgumentException("지하철 이동거리가 잘못되었습니다.");
		}
		this.totalDistance = totalDistance;
	}

	/**
	 * 요금 계산 방법
	 * 기본운임(10㎞ 이내) : 기본운임 1,250원
	 * 이용 거리초과 시 추가운임 부과
	 * 10km초과∼50km까지(5km마다 100원)
	 * 50km초과 시 (8km마다 100원)
	 */
	@Override
	public long calculateFee() {

		if (this.totalDistance < BASE_DISTANCE) {
			return BASIC_FEE;
		}

		return BASIC_FEE
			+ calculateOverFare(getDivisionOneDistance(), DIVISION_ONE_BASE_DISTANCE)
			+ calculateOverFare(getDivisionOverOneDistance(), OVER_DIVISION_ONE_BASE_DISTANCE);
	}

	private int getDivisionOneDistance() {
		if (this.totalDistance <= BASE_DISTANCE) {
			return 0;
		}
		return this.totalDistance - BASE_DISTANCE - (getOverDistanceThanDivisionOne());

	}

	private int getOverDistanceThanDivisionOne() {
		if (this.totalDistance - DIVISION_ONE_DISTANCE <= 0) {
			return 0;
		}
		return this.totalDistance - DIVISION_ONE_DISTANCE;
	}

	private int getDivisionOverOneDistance() {
		if (this.totalDistance <= DIVISION_ONE_DISTANCE) {
			return 0;
		}

		return this.totalDistance - DIVISION_ONE_DISTANCE;

	}

	private int calculateOverFare(int distance, int baseOfDistance) {
		if (distance <= 0) {
			return 0;
		}
		return (int)((Math.ceil((distance - 1) / baseOfDistance) + 1) * 100);
	}
}
