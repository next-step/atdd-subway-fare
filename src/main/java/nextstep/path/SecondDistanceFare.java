package nextstep.path;

import nextstep.path.domain.Fare;
import org.springframework.stereotype.Component;

@Component
public class SecondDistanceFare implements DistanceFare {

    public static final int RANGE_END_EXCLUSIVE = 50;
    public static final int RANGE_START_EXCLUSIVE = 10;
    public static final int THRESHOLD = 40;

    @Override
    public int calculateFare(Fare fare) {
        int remain = getRemain(fare.getDistance());
        if (remain == 0 || fare.getDistance() <= RANGE_START_EXCLUSIVE) {
            return 0;
        }
        return (int) ((Math.ceil((remain - 1) / 5) + 1) * 100);
    }

    private int getRemain(int distance) {
        if (distance > RANGE_END_EXCLUSIVE) {
            return THRESHOLD;
        }
        return distance - RANGE_START_EXCLUSIVE;
    }
}
