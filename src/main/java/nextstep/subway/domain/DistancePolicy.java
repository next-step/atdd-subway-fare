package nextstep.subway.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum DistancePolicy {
    STEP0(1, 0),
    STEP1(11, 5),
    STEP2(51, 8);

    private final int boundary;
    private final int distance;

    DistancePolicy(int boundary, int distance) {
        this.boundary = boundary;
        this.distance = distance;
    }

    public int getBoundary() {
        return boundary;
    }

    public int getDistance() {
        return distance;
    }

    public static DistancePolicy decide(int totalDistance) {
        List<DistancePolicy> standards = Arrays.asList(values());
        Collections.reverse(standards);

        return standards.stream()
                .filter(s -> s.isWithinBoundary(totalDistance))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    private boolean isWithinBoundary(int totalDistance) {
        return totalDistance >= boundary;
    }

    public boolean isBasicStandard() {
        return this.equals(STEP0);
    }

    public int calculateOverDistance(int totalDistance) {
        return totalDistance - getBasicBoundary();
    }

    private int getBasicBoundary() {
        return STEP1.boundary - 1;
    }
}
