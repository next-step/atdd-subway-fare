package nextstep.subway.domain.farepolicy;

public class BasicFarePolicy implements FarePolicy {
    private static final int BASIC_COST = 1250;

    @Override
    public int calculate(int distance) {
        return BASIC_COST;
    }
}
