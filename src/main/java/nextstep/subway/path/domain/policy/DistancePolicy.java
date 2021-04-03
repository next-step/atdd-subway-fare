package nextstep.subway.path.domain.policy;

public abstract class DistancePolicy {

	protected static final int DEFAULT_FARE = 1250;
	protected static final int FIRST_SECTION_LENGTH = 10;
	protected static final int FIRST_SURCHARGE_LENGTH = 5;
	protected static final int SECOND_SECTION_LENGTH = 50;
	protected static final int SECTION_SURCHARGE_LENGTH = 8;

	public abstract boolean isSupport(int distance);
	public abstract int getFare(int distance);

	protected int calculateOverFare(int distance, int sectionLength) {
		return (int) ((Math.ceil((distance - 1) / sectionLength) + 1) * 100);
	}
}
