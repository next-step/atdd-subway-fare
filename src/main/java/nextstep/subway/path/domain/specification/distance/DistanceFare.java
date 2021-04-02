package nextstep.subway.path.domain.specification.distance;

import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;

public class DistanceFare implements FarePolicy {
    private Distance dist;

    public DistanceFare(Distance distance){
        this.dist = distance;
    }

    private int calculate10KmOverFare() {
        if (dist.getDistance() < 10) {
            return 0;
        }
        return (int) ((Math.ceil((dist.getDistance() - 10) / 5) + 1) * 100);
    }

    private int calculate50KmOverFare() {
        if (dist.getDistance() < 50) {
            return 0;
        }
        return (int) ((Math.ceil((dist.getDistance() - 50) / 8) + 1) * 100);
    }

    @Override
    public Fare calculate(Fare fare) {
        final int overFare = calculate10KmOverFare() + calculate50KmOverFare();
        return Fare.sum(fare, Fare.of(overFare));
    }
}
