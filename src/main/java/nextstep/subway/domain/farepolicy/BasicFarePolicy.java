package nextstep.subway.domain.farepolicy;

public class BasicFarePolicy implements FarePolicy {
    @Override
    public int calculate(int distance) {
        return 1250;
    }
}
