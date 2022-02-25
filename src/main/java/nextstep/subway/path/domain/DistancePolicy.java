package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public class DistancePolicy implements FarePolicy{

    private final int distance;
    private final Policy policy;

    private DistancePolicy(int distance) {
        this.distance = distance;
        this.policy = Policy.choicePolicyByDistance(distance);
    }

    public static DistancePolicy from(int distance) {
        return new DistancePolicy(distance);
    }

    enum Policy {
        BASE(0, 0, 0, distance -> 0 <= distance && distance <= 10),
        BY_50KM(10, 100, 5, distance -> 11 <= distance && distance <= 50),
        OVER_50KM(50, 100, 8, distance -> 51 <= distance && distance < Integer.MAX_VALUE)
        ;

        private static final int BASE_MAX_FARE = 1250;

        private final int parentMaxDistance;
        private final int extraCharge;
        private final int perDistance;
        private final Function<Integer, Boolean> expression;

        Policy(int parentMaxDistance, int extraCharge, int perDistance, Function<Integer, Boolean> expression) {
            this.parentMaxDistance = parentMaxDistance;
            this.extraCharge = extraCharge;
            this.perDistance = perDistance;
            this.expression = expression;
        }

        public static Policy choicePolicyByDistance(int distance) {
            return Arrays.stream(Policy.values())
                    .filter(it -> it.expression.apply(distance))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        private int getParentPolicyMaxFare() {
            Policy parentPolicy = Policy.choicePolicyByDistance(parentMaxDistance);
            return parentPolicy.calculate(parentMaxDistance);
        }

        public int calculate(int distance) {
            if(parentMaxDistance == 0) {
                return BASE_MAX_FARE;
            }

            return getParentPolicyMaxFare() +
                    (int) ((Math.ceil((distance - parentMaxDistance - 1) / perDistance) + 1) * extraCharge);
        }
    }

    @Override
    public Fare getFare(Fare fare) {
        fare.add(this.policy.calculate(this.distance));
        return fare;
    }

}
