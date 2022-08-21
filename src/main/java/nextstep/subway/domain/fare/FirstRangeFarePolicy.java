package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

@Component
public class FirstRangeFarePolicy implements FarePolicy {

    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final float FIRST_UNIT_DISTANCE = 5f;
    private static final int RANGE_START = 10;
    private static final int RANGE_ENDS = 50;

    @Override
    public int fare(Path path) {
        int targetDistance = getTargetDistance(path.extractDistance());
        return getFirstRangeOverFare(targetDistance);
    }

    private int getTargetDistance(int distance) {
        return Math.min(RANGE_ENDS, distance) - RANGE_START;
    }

    private int getFirstRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / FIRST_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }

}
