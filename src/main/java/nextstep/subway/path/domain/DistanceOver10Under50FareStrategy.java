package nextstep.subway.path.domain;

public class DistanceOver10Under50FareStrategy implements FareCalculationStrategy {
    @Override
    public int calculate(int distance) {
        return 0;
    }
}
