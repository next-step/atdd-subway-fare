package nextstep.subway.domain.farechain;

import nextstep.subway.domain.Path;

public class DistanceOverFare extends OverFarePolicyHandler {
    private static final int DEFAULT_FARE_DISTANCE = 10;

    public DistanceOverFare(OverFarePolicyHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public int chargeOverFare(Path path) {
        return getOverDistanceFare(path.extractDistance());
    }

    public static int getOverDistanceFare(int distance) {

        if (distance > DEFAULT_FARE_DISTANCE && distance <= 50) {
            return calculateOverFareUnder50(distance - DEFAULT_FARE_DISTANCE);
        }

        if (distance > 50) {
            return calculateOverFareOver50(distance - DEFAULT_FARE_DISTANCE);
        }

        return 0;
    }

    private static int calculateOverFareUnder50(int overDistance) {

        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }

    private static int calculateOverFareOver50(int overDistance) {

        int chargeDistance = overDistance - 40;
        return calculateOverFareUnder50(40) + (int) ((Math.ceil((chargeDistance - 1) / 8) + 1) * 100);
    }

}
