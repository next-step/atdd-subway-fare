package nextstep.subway.domain.farepolicy;

public class UpTo50FarePolicy implements FarePolicy {
    @Override
    public int calculate(int distance) {
        return 0;
    }
}
