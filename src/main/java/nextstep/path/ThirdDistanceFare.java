package nextstep.path;


import nextstep.path.domain.Fare;
import org.springframework.stereotype.Component;

public class ThirdDistanceFare implements DistanceFare {
    public static final int RANGE_START_EXCLUSIVE = 50;

    @Override
    public int calculateFare(Fare fare) {
        int remain = getRemain(fare.getDistance());
        if (remain == 0 || fare.getDistance() <= RANGE_START_EXCLUSIVE) {
            return 0;
        }
        return (int) ((Math.ceil((remain - 1) / 8) + 1) * 100);
    }

    private int getRemain(int distance) {
        return distance - RANGE_START_EXCLUSIVE;
    }
}
