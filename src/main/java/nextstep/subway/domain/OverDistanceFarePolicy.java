package nextstep.subway.domain;

import java.util.Arrays;

public enum OverDistanceFarePolicy {
    OVER_TEN_FARE(10, 50, 100, 5),
    OVER_FIFTY_FARE(50, Integer.MAX_VALUE, 100, 8);

    private int minKm;
    private int maxKm;
    private int farePer;
    private int perKm;

    OverDistanceFarePolicy(int minKm, int maxKm, int farePer, int perKm) {
        this.minKm = minKm;
        this.maxKm = maxKm;
        this.farePer = farePer;
        this.perKm = perKm;
    }

    public static Fare calculateOverDistanceFare(int distance) {
        return Arrays.stream(values())
                .map(it -> calculateOverDistanceFare(it, distance))
                .reduce(Fare.of(0), Fare::plus);
    }

    private static Fare calculateOverDistanceFare(OverDistanceFarePolicy policy, int totalDistance) {
        int overDistance = Math.max(Math.min(totalDistance, policy.maxKm) - policy.minKm, 0);
        return calculateOverDistanceFare(overDistance, policy.perKm, policy.farePer);
    }

    private static Fare calculateOverDistanceFare(int overDistance, int perKm, int farePer) {
        int overFare = (int) (Math.ceil((overDistance + perKm - 1) / perKm) * farePer);
        return Fare.of(overFare);
    }
}
