package nextstep.subway.path.domain.policy;

public class OverTenDistancePolicy implements FarePolicy {

    private final int distance;

    public OverTenDistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return 0;
    }
}
