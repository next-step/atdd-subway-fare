package nextstep.subway.maps.map.application;

import org.springframework.stereotype.Component;

import nextstep.subway.maps.map.domain.SubwayPath;

@Component
public class FareCalculator {

    public static final int BASIC_FARE = 1250;

    public static final int OVERFARE_DISTANCE_THRESHOLD = 10;
    public static final int SUPER_OVERFARE_DISTANCE_THRESHOLD = 50;
    public static final int OVERFARE_DISTANCE_DIVIDEND = 5;

    public static final int SUPER_OVERFARE_DISTANCE_DIVIDEND = 8;
    public static final int OVERFARE_ADDED_PRICE_PER_KM = 100;

    public int calculate(int distance) {
        int overFare = 0;
        if (distance > SUPER_OVERFARE_DISTANCE_THRESHOLD) {
            int overDistance = distance - SUPER_OVERFARE_DISTANCE_THRESHOLD;
            overFare = overFare + calculateOverFare(overDistance, SUPER_OVERFARE_DISTANCE_DIVIDEND);
            distance = distance - overDistance;
        }
        if (distance > OVERFARE_DISTANCE_THRESHOLD) {
            int overDistance = distance - OVERFARE_DISTANCE_THRESHOLD;
            overFare = overFare + calculateOverFare(overDistance, OVERFARE_DISTANCE_DIVIDEND);
        }
        return BASIC_FARE + overFare;
    }

    public int calculate(SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();
        int overFare = 0;
        if (distance > SUPER_OVERFARE_DISTANCE_THRESHOLD) {
            int overDistance = distance - SUPER_OVERFARE_DISTANCE_THRESHOLD;
            overFare = overFare + calculateOverFare(overDistance, SUPER_OVERFARE_DISTANCE_DIVIDEND);
            distance = distance - overDistance;
        }
        if (distance > OVERFARE_DISTANCE_THRESHOLD) {
            int overDistance = distance - OVERFARE_DISTANCE_THRESHOLD;
            overFare = overFare + calculateOverFare(overDistance, OVERFARE_DISTANCE_DIVIDEND);
        }
        return BASIC_FARE + overFare;
    }

    private int calculateOverFare(int overDistance, int unit) {
        return (int)((Math.ceil(overDistance - 1) / unit) + 1) * OVERFARE_ADDED_PRICE_PER_KM;
    }
}
