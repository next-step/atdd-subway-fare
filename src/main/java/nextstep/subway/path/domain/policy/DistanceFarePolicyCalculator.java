package nextstep.subway.path.domain.policy;

import nextstep.subway.path.enums.DistanceFarePolicy;

public class DistanceFarePolicyCalculator extends FarePolicyCalculator {

    private final int distance;

    public DistanceFarePolicyCalculator(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate(int total) {
        return total + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        int over10KmFare = calculateOverDistanceFare(DistanceFarePolicy.TEN_KM, distance);
        int over50KmFare = calculateOverDistanceFare(DistanceFarePolicy.FIFTY_KM, distance);
        return over10KmFare + over50KmFare;
    }

    private int calculateOverDistanceFare(DistanceFarePolicy distanceFarePolicy, int distance) {
        distance -= distanceFarePolicy.getOverChargeDistance();
        if (distance > 0) {
            int calculatedFare = (int) ((Math.ceil((distance - 1) / distanceFarePolicy.doChargeEveryNKm()) + 1) * 100);
            return Math.min(distanceFarePolicy.getMaximumFare(), calculatedFare);
        }
        return 0;
    }
}
