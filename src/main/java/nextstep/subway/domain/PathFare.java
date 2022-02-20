package nextstep.subway.domain;

import java.util.Arrays;

public enum PathFare {
    BASIC(0, 10, 0, 1250),
    SHORT(11, 50, 5, 100),
    LONG(51, Integer.MAX_VALUE,8,  100);

    private int standardDistance;
    private int maxDistance;
    private int fareByDistance;
    private int distanceExceeded;

    PathFare(int standardDistance, int maxDistance, int fareByDistance, int distanceExceeded) {
        this.standardDistance = standardDistance;
        this.maxDistance = maxDistance;
        this.fareByDistance = fareByDistance;
        this.distanceExceeded = distanceExceeded;
    }

    public static int extractFare(int distance) {
        return Arrays.stream(PathFare.values())
                .mapToInt(pathFare -> pathFare.calculateOverFare(distance))
                .sum();
    }

    private int calculateOverFare(int distance) {
        if (this == PathFare.BASIC) {
            return distanceExceeded;
        }

        if (distance < standardDistance) {
            return 0;
        }

        int overDistance = Integer.min(distance, maxDistance);
        return (int) ((Math.ceil((overDistance - standardDistance) / fareByDistance) + 1) * distanceExceeded);
    }
}
