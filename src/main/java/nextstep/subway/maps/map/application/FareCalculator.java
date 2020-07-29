package nextstep.subway.maps.map.application;

import nextstep.subway.maps.map.domain.SubwayPath;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE_DISTANCE = 10;
    private static final int SUPER_OVER_FARE_DISTANCE = 50;
    public static final int ADDITIONAL_FARE = 100;
    public static final int SUPER_OVER_FARE_CHARGE_UNIT = 8;
    public static final int OVER_FARE_CHARGE_UNIT = 5;

    public int calculate(int distance) {
        int overFare = 0;
        if (distance > SUPER_OVER_FARE_DISTANCE) {
            int overDistance = distance - SUPER_OVER_FARE_DISTANCE;
            overFare += calculateOverFare(overDistance, SUPER_OVER_FARE_CHARGE_UNIT);
            distance -= overDistance;
        }
        if (distance > OVER_FARE_DISTANCE) {
            int overDistance = distance - OVER_FARE_DISTANCE;
            overFare += calculateOverFare(overDistance, OVER_FARE_CHARGE_UNIT);
        }
        return BASIC_FARE + overFare;
    }

    public int calculate(SubwayPath path) {
        int distance = path.calculateDistance();


        int overFare = 0;
        if (distance > SUPER_OVER_FARE_DISTANCE) {
            int overDistance = distance - SUPER_OVER_FARE_DISTANCE;
            overFare += calculateOverFare(overDistance, SUPER_OVER_FARE_CHARGE_UNIT);
            distance -= overDistance;
        }
        if (distance > OVER_FARE_DISTANCE) {
            int overDistance = distance - OVER_FARE_DISTANCE;
            overFare += calculateOverFare(overDistance, OVER_FARE_CHARGE_UNIT);
        }
        return BASIC_FARE + overFare;
    }

    private int calculateOverFare(int overDistance, int unit) {
        return (int) ((Math.ceil((overDistance - 1) / unit) + 1) * ADDITIONAL_FARE);
    }


}
