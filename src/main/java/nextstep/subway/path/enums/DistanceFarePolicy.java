package nextstep.subway.path.enums;

public enum DistanceFarePolicy {

    TEN_KM(10, 5, 800),
    FIFTY_KM(50, 8, Integer.MAX_VALUE);

    final private int overChargeDistance;
    final private int chargeEveryNKm;
    final private int maximumFare;

    DistanceFarePolicy(int overChargeDistance, int chargeEveryNKm, int maximumFare) {
        this.overChargeDistance = overChargeDistance;
        this.chargeEveryNKm = chargeEveryNKm;
        this.maximumFare = maximumFare;
    }

    public int getOverChargeDistance() {
        return overChargeDistance;
    }

    public int doChargeEveryNKm() {
        return chargeEveryNKm;
    }

    public int getMaximumFare() {
        return maximumFare;
    }
}
