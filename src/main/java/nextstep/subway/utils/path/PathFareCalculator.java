package nextstep.subway.utils.path;

import org.springframework.stereotype.Component;

@Component
public class PathFareCalculator {

    private static final int BASE_FARE = 1250;

    public static Integer calculate(long distance) {

        if (distance > 10 && distance <= 50) {
            return BASE_FARE + calculateOver10KmFare(distance - 10);
        }

        if (distance > 50L) {
            return BASE_FARE + calculateOver10KmFare(40)
                + calculateOver50KmFare(distance - 50);
        }

        return BASE_FARE;
    }

    private static Integer calculateOver10KmFare(long distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static Integer calculateOver50KmFare(long distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
