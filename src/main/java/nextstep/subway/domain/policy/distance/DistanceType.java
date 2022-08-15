package nextstep.subway.domain.policy.distance;

import java.util.Arrays;
import java.util.function.Function;

enum DistanceType {
    DEFAULT(0, 10, 0, 0, Policy -> defaultCalculateFare()),
    OVER_10KM(10, 50, 5, 100, DistanceType::calculateFare10),
    OVER_50KM(50, Integer.MAX_VALUE, 8, 100, DistanceType::calculateFare50);

    private static final int BASIC_FARE = 1250;
    private final int minDistance;
    private final int maxDistance;
    private final int extraFare;
    private final int extraDistance;
    private final Function<Integer, Integer> expression;

    DistanceType(int minDistance, int maxDistance, int extraDistance, int extraFare,
           Function<Integer, Integer> expression) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.extraDistance = extraDistance;
        this.extraFare = extraFare;
        this.expression = expression;
    }

    public static DistanceType createFarePolicy(int distance) {
        return Arrays.stream(DistanceType.values())
                .filter(fare -> fare.isWithinDistance(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isWithinDistance(int distance) {
        return minDistance < distance && distance <= maxDistance;
    }

    public int calculate(int distance) {
        return expression.apply(distance);
    }

    private static int defaultCalculateFare() {
        return BASIC_FARE;
    }

    private static int calculateFare10(int distance) {
        int between10And50distance = distance - OVER_10KM.minDistance;
        return BASIC_FARE + calculateOver10ExtraFare(between10And50distance);
    }

    private static int calculateFare50(int distance) {
        int fare = BASIC_FARE;

        int between10And50distance = OVER_10KM.maxDistance - OVER_10KM.minDistance;
        fare += calculateOver10ExtraFare(between10And50distance);

        int over50Distance = distance - OVER_50KM.minDistance;
        fare += calculateOver50ExtraFare(over50Distance);

        return fare;
    }

    private static int calculateOver10ExtraFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / OVER_10KM.extraDistance) + 1) * OVER_10KM.extraFare);
    }

    private static int calculateOver50ExtraFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / OVER_50KM.extraDistance) + 1) * OVER_50KM.extraFare);
    }
}
