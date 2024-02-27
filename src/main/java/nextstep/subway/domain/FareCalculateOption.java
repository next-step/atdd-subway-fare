package nextstep.subway.domain;

public class FareCalculateOption {
    private final int distanceOver;
    private final int distanceUnder;
    private final int chargingUnitDistance;
    private final int fare;

    public FareCalculateOption(int distanceOver, int distanceUnder, int step, int fare) {
        this.distanceOver = distanceOver;
        this.distanceUnder = distanceUnder;
        this.chargingUnitDistance = step;
        this.fare = fare;
    }

    public int getChargingUnitDistance() {
        return chargingUnitDistance;
    }

    public int getFare() {
        return fare;
    }

    public boolean isCalculateTarget(int distance) {
        return distance > distanceOver;
    }

    public int getOverDistance(int distance) {
        if (isUpperboundValue(distance)) {
            return distanceUnder - distanceOver;
        }

        return distance - distanceOver;
    }

    private boolean isUpperboundValue(int distance) {
        return distance > distanceUnder;
    }

}
