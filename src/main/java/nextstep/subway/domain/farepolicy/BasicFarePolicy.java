package nextstep.subway.domain.farepolicy;

public class BasicFarePolicy implements FarePolicy {
    @Override
    public int calculate() {
        return 1250;
    }
}
