package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

@Component
public class SecondRangeFarePolicy implements FarePolicy {

    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final int RANGE_START = 50;
    private static final float SECOND_UNIT_DISTANCE = 8f;

    @Override
    public int fare(Path path) {
        int targetDistance = getTargetDistance(path.extractDistance());
        return getSecondRangeOverFare(targetDistance);
    }

    private int getTargetDistance(int distance) {
        return distance - RANGE_START;
    }

    private int getSecondRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / SECOND_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }
}
