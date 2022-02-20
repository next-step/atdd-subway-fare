package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;

import java.util.Arrays;
import java.util.function.Function;

public enum DistancePolicy implements FarePolicy {
    BASE(0, 0, 0, distance -> 0 <= distance && distance <= 10),
    BY_50KM(10, 100, 5, distance -> 11 <= distance && distance <= 50),
    OVER_50KM(50, 100, 8, distance -> 51 <= distance && distance < Integer.MAX_VALUE)
    ;

    private static final int BASE_MAX_FARE = 1250;

    private final int parentMaxDistance;
    private final int extraCharge;
    private final int perDistance;
    private final Function<Integer, Boolean> expression;

    DistancePolicy(int parentMaxDistance, int extraCharge, int perDistance, Function<Integer, Boolean> expression) {
        this.parentMaxDistance = parentMaxDistance;
        this.extraCharge = extraCharge;
        this.perDistance = perDistance;
        this.expression = expression;
    }

    public static DistancePolicy choicePolicyByDistance(int distance) {
        return Arrays.stream(DistancePolicy.values())
                .filter(it -> it.expression.apply(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private int getParentPolicyMaxFare() {
        DistancePolicy parentPolicy = DistancePolicy.choicePolicyByDistance(parentMaxDistance);
        FarePolicyRequest parentFarePolicyRequest = FarePolicyRequest.builder()
                .distance(parentMaxDistance)
                .build();

        return parentPolicy.calculate(parentFarePolicyRequest);
    }

    @Override
    public int calculate(FarePolicyRequest request) {
        if(parentMaxDistance == 0) {
            return BASE_MAX_FARE;
        }

        int distance = request.getDistance() - parentMaxDistance;
        return getParentPolicyMaxFare() +
                (int) ((Math.ceil((distance - 1) / perDistance) + 1) * extraCharge);
    }
}
