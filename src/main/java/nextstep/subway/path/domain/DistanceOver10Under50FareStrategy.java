package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.FareCalculationStrategyFactory.DEFAULT_FARE_DISTANCE;

public class DistanceOver10Under50FareStrategy implements FareCalculationStrategy {

    private int distance;

    public DistanceOver10Under50FareStrategy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return DEFAULT_FARE + (int) ((Math.ceil((distance - DEFAULT_FARE_DISTANCE - 1) / 5) + 1) * 100);
    }
}
