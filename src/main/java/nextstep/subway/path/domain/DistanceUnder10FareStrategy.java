package nextstep.subway.path.domain;

public class DistanceUnder10FareStrategy implements FareCalculationStrategy {
    @Override
    public int calculate(int distance) {
        return 0;
    }
}
