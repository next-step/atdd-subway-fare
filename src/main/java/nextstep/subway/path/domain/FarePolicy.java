package nextstep.subway.path.domain;

import java.util.EnumSet;

public class FarePolicy {

    private static final long DEFAULT_FARE = 1250L;

    private FarePolicy() {
    }

    public static Long calculate(Long distance) {
        return DEFAULT_FARE + EnumSet.allOf(Fare.class).stream()
                .filter(fare -> distance > fare.getStartDistance())
                .mapToInt(fare -> calculate(distance, fare))
                .sum();
    }

    private static int calculate(Long distance,
                                 Fare fare) {
        long applicableDistance = Math.min(distance, fare.getEndDistance()) - fare.getStartDistance();
        return (int) ((Math.ceil((double) applicableDistance / fare.getDistanceUnit())) * fare.getUnitFare());
    }
}
