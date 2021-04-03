package nextstep.subway.path.domain.policy;

public class SecondOverDistancePolicy extends DistancePolicy {

	@Override
	public boolean isSupport(int distance) {
		return distance > SECOND_SECTION_LENGTH;
	}

	@Override
	public int getFare(int distance) {
		return calculateOverFare(distance - SECOND_SECTION_LENGTH, SECTION_SURCHARGE_LENGTH);
	}
}
