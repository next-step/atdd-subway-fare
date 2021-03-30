package nextstep.subway.path.domain;

public class DistanceUnder10FareStrategy implements FareCalculationStrategy {

    private int distance;

    public DistanceUnder10FareStrategy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return DEFAULT_FARE;
    }
}
