package nextstep.subway.path.domain.policy;

public class DefaultDistancePolicy extends DistancePolicy {

	@Override
	public boolean isSupport(int distance) {
		return true;
	}

	@Override
	public int getFare(int distance) {
		return DEFAULT_FARE;
	}
}
