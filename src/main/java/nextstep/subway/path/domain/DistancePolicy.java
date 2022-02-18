package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;

import java.util.Arrays;

public enum DistancePolicy implements FarePolicy {
    BASE(null, 0, 0, 1, 11),
    BY50km(BASE, 100, 5, 11, 51),
    OVER50km(BY50km, 100, 8, 51, Integer.MAX_VALUE)
    ;

    private final DistancePolicy parentPolicy;
    private final int extraCharge;
    private final int perDistance;
    private final int minDistance;
    private final int maxDistance;

    DistancePolicy(DistancePolicy parentPolicy, int extraCharge, int perDistance, int minDistance, int maxDistance) {
        this.parentPolicy = parentPolicy;
        this.extraCharge = extraCharge;
        this.perDistance = perDistance;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    public static DistancePolicy choicePolicyByDistance(int distance) {
        return Arrays.stream(DistancePolicy.values())
                .filter(it -> it.isInDistanceRange(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isInDistanceRange(int distance) {
        return minDistance <= distance  && distance < maxDistance;
    }

    @Override
    public int calculate(FarePolicyRequest request) {
        if(parentPolicy == null) {
            return 1250;
        }

        int parentMaxDistance = parentPolicy.maxDistance - 1;
        int distance = request.getDistance() - parentMaxDistance;

        FarePolicyRequest parentFarePolicyRequest = FarePolicyRequest.builder()
                .distance(parentMaxDistance)
                .build();

        return parentPolicy.calculate(parentFarePolicyRequest) +
                (int) ((Math.ceil((distance - 1) / perDistance) + 1) * extraCharge);
    }
}
