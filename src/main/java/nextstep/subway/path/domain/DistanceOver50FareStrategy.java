package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.FareCalculationStrategyFactory.ADD_100_FARE_DISTANCE;
import static nextstep.subway.path.domain.FareCalculationStrategyFactory.DEFAULT_FARE_DISTANCE;

public class DistanceOver50FareStrategy implements FareCalculationStrategy {

    private int distance;

    public DistanceOver50FareStrategy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return DEFAULT_FARE + (int) ((Math.ceil((distance - DEFAULT_FARE_DISTANCE - 1) / 5) + 1) * 100)
                + (int) ((Math.ceil((distance - ADD_100_FARE_DISTANCE - 1) / 8) + 1) * 100);
    }
}
