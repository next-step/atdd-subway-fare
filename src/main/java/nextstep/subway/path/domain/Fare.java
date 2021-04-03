package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.DistanceRule.PER_5KM;
import static nextstep.subway.path.domain.DistanceRule.PER_8KM;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int OVER_FARE = 100;
    private int fare;

    public Fare(Distance distance) {
        fare = BASIC_FARE
                + calculateOverFare(PER_5KM.getRestDistance(distance), PER_5KM.getUnitDistance())
                + calculateOverFare(PER_8KM.getRestDistance(distance), PER_8KM.getUnitDistance());
    }

    public int getFare(){
        return fare;
    }

    private int calculateOverFare(Distance distance, Distance unitDistance) {
        if(distance.toIntValue() == 0) return 0;
        return (int) ((Math.ceil((distance.toIntValue() - 1) / unitDistance.toIntValue()) + 1) * OVER_FARE);
    }
}
