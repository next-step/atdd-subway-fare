package nextstep.subway.domain;

import lombok.val;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Fare {

    private static final BigDecimal DEFAULT_FARE = BigDecimal.valueOf(1250);

    private final int maximumPolicyIndex;
    private final int distance;
    private static final List<DistancePolicy> distancePolicies = new ArrayList<DistancePolicy>() {
        {
            add(new DistancePolicy(5, 0, 0));
            add(new DistancePolicy(5, 100, 11));
            add(new DistancePolicy(8, 100, 51));
        }
    };

    private static class DistancePolicy {
        private final int perDistance;
        private final int minDistance;
        private final BigDecimal extraFare;

        DistancePolicy(int perDistance, int extraFare, int minDistance) {
            this.perDistance = perDistance;
            this.extraFare = BigDecimal.valueOf(extraFare);
            this.minDistance = minDistance;
        }
    }

    public Fare(int distance) {
        this.distance = distance;
        this.maximumPolicyIndex = getMaximumPolicyIndex(distancePolicies.size() - 1);
    }

    private int getMaximumPolicyIndex(int depth) {
        val distancePolicy = distancePolicies.get(depth);
        val extraDistance = distance - distancePolicy.minDistance + 1;
        if (extraDistance > 0) {
            return depth;
        }

        return getMaximumPolicyIndex(depth - 1);
    }

    public BigDecimal getFare() {
        BigDecimal extraFare = getExtraFare(
                BigDecimal.ZERO,
                maximumPolicyIndex,
                0
        );

        return DEFAULT_FARE.add(extraFare);
    }

    private BigDecimal getExtraFare(BigDecimal extraFare, int depth, int beforePolicyMinDistance) {
        val policy = distancePolicies.get(depth);
        int defaultOrBeforeDistance = beforePolicyMinDistance != 0 ? beforePolicyMinDistance : distance;
        val extraDistance = defaultOrBeforeDistance - policy.minDistance + 1;
        BigDecimal addedExtraFare = extraFare.add(calculateOverFare(extraDistance, policy));

        if (depth == 0) {
            return addedExtraFare;
        }

        return getExtraFare(addedExtraFare, depth - 1, policy.minDistance);
    }

    private BigDecimal calculateOverFare(int distance, DistancePolicy policy) {
        return BigDecimal.valueOf(Math.ceil((distance - 1) / policy.perDistance) + 1).multiply(policy.extraFare);
    }
}
