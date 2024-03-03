package nextstep.subway.domain.fareOption;

public class FareCalculateOption {
    private int distanceOver;
    private int distanceUnder;
    private int chargingUnitDistance;
    private int fare;

    public FareCalculateOption() {
    }

    public FareCalculateOption(int distanceOver, int distanceUnder, int chargingUnitDistance, int fare) {
        this.distanceOver = distanceOver;
        this.distanceUnder = distanceUnder;
        this.chargingUnitDistance = chargingUnitDistance;
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

    public FareCalculateOption setDistanceOver(int distanceOver) {
        this.distanceOver = distanceOver;
        return this;
    }

    public FareCalculateOption setDistanceUnder(int distanceUnder) {
        this.distanceUnder = distanceUnder;
        return this;
    }

    public FareCalculateOption setChargingUnitDistance(int chargingUnitDistance) {
        this.chargingUnitDistance = chargingUnitDistance;
        return this;
    }

    public FareCalculateOption setFare(int fare) {
        this.fare = fare;
        return this;
    }

    public FareCalculateOption build() {
        return this;
    }

    private boolean isUpperboundValue(int distance) {
        return distance > distanceUnder;
    }

}
