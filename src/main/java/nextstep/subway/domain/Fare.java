package nextstep.subway.domain;

public class Fare {

    private static int DEFAULT_FARE = 1250;
    private static int BASIC_DISTANCE = 10;
    private static int MID_RANGE = 10;
    private static int MID_RANGE_PER_DISTANCE = 5;
    private static int BIG_RANGE = 50;
    private static int BIG_RANGE_PER_DISTANCE = 8;
    private static int ADDITONAL_FEE_PER_DISTANCE = 100;

    public int getFareByDistance(int distance) {

        if (distance > BIG_RANGE) {
            return DEFAULT_FARE + calculateOverFare(distance, BIG_RANGE_PER_DISTANCE);
        }

        if (distance > MID_RANGE) {
            return DEFAULT_FARE + calculateOverFare(distance, MID_RANGE_PER_DISTANCE);
        }

        return DEFAULT_FARE;
    }

    private int calculateOverFare(int distance, int perDistance) {
        return (((distance - BASIC_DISTANCE - 1) / perDistance) + 1) * ADDITONAL_FEE_PER_DISTANCE;
    }
}
