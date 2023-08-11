package nextstep.subway.domain.farechain;

public class DistanceOverFare extends OverFarePolicyHandler {
    private static final int DEFAULT_FARE_DISTANCE = 10;

    private final int distance;

    public DistanceOverFare(int distance) {
        super(null);
        this.distance = distance;
    }



    @Override
    public int chargeOverFare(int fare) {
        return super.chargeHandler(fare + getOverDistanceFare());
    }

    public int getOverDistanceFare() {

        if (distance > DEFAULT_FARE_DISTANCE && distance <= 50) {
            return calculateOverFareUnder50(distance - DEFAULT_FARE_DISTANCE);
        }

        if (distance > 50) {
            return calculateOverFareOver50(distance - DEFAULT_FARE_DISTANCE);
        }

        return 0;
    }

    private int calculateOverFareUnder50(int overDistance) {

        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }

    private int calculateOverFareOver50(int overDistance) {

        int chargeDistance = overDistance - 40;
        return calculateOverFareUnder50(40) + (int) ((Math.ceil((chargeDistance - 1) / 8) + 1) * 100);
    }

}
