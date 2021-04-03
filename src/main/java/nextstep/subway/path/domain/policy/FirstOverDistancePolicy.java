package nextstep.subway.path.domain.policy;

public class FirstOverDistancePolicy extends DistancePolicy {

	@Override
	public boolean isSupport(int distance) {
		return distance > FIRST_SECTION_LENGTH;
	}

	@Override
	public int getFare(int distance) {
		return calculateOverFare(getPolicyDistance(distance) - FIRST_SECTION_LENGTH, FIRST_SURCHARGE_LENGTH);
	}

	private int getPolicyDistance(int distance) {
		return Math.min(distance, SECOND_SECTION_LENGTH);
	}
}
