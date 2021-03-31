package nextstep.subway.path.domain.policy;

public class OverFiftyDistancePolicy implements FarePolicy {

    private final int distance;

    public OverFiftyDistancePolicy(int distance) {
        this.distance = distance;
    }
}
