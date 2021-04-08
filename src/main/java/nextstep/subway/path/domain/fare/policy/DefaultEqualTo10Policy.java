package nextstep.subway.path.domain.fare.policy;

public class DefaultEqualTo10Policy implements FarePolicy {

    @Override
    public int calculate(int fare, int distance) {
        return fare;
    }
}
