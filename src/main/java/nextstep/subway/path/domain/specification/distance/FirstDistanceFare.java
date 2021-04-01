package nextstep.subway.path.domain.specification.distance;

import nextstep.subway.path.domain.DistanceFare;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

public class FirstDistanceFare implements DistanceFare {
    private int calculate10KmOverFare(int distance) {
        if (distance < 10) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 10) / 5) + 1) * 100);
    }

    private int calculate50KmOverFare(int distance) {
        if (distance < 50) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 50) / 8) + 1) * 100);
    }

    @Override
    public Fare calculate(Distance dist) {
        int fare = BASE_FARE + calculate10KmOverFare(dist.getDistance()) + calculate50KmOverFare(dist.getDistance());
        return Fare.of(fare);
    }
}
