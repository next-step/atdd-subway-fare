package nextstep.subway.path.domain;

public class DistanceFarePolicy implements FarePolicy {
    private static final int DISTANCE_TEN = 10;
    private static final int DISTANCE_FIFTY = 50;
    private static final int ADDITIONAL_FARE_PER_DISTANCE = 100;
    private static final int ONE = 1;
    private static final int PER_EIGHT = 8;
    private static final int PER_FIVE = 5;
    private static final int ELEVEN = 11;
    private static final int MAX_UNTIL_FIFTY = 800;
    private static final int FIFTY_ONE = 51;

    private int distance;

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateFareByPolicy(int subtotal) {
        if(distance <= DISTANCE_TEN) {
            return subtotal;
        }

        if(distance > DISTANCE_FIFTY) {
            subtotal += MAX_UNTIL_FIFTY + (int) ((Math.ceil((distance - FIFTY_ONE) / PER_EIGHT) + ONE) * ADDITIONAL_FARE_PER_DISTANCE);
            return subtotal;
        }

        subtotal += (int) ((Math.ceil((distance - ELEVEN) / PER_FIVE) + ONE) * ADDITIONAL_FARE_PER_DISTANCE);
        return subtotal;
    }
}




