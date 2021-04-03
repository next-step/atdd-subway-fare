package nextstep.subway.path.domain;

public enum FarePolicy {

    TENKM(10, 5, 800),
    FIFTYKM(50, 8, Integer.MAX_VALUE);

    final private int overChargeDistance;
    final private int chargeEveryNKm;
    final private int maximumFare;

    FarePolicy(int overChargeDistance, int chargeEveryNKm, int maximumFare) {
        this.overChargeDistance = overChargeDistance;
        this.chargeEveryNKm = chargeEveryNKm;
        this.maximumFare = maximumFare;
    }

    public int getOverChargeDistance() {
        return overChargeDistance;
    }

    public int takeChargeEveryNKm() {
        return chargeEveryNKm;
    }

    public int getMaximumFare() {
        return maximumFare;
    }
}
