package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public class DistancePolicy implements FarePolicy {

    private final int distance;

    public static DistancePolicy from(final int distance) {
        return new DistancePolicy(distance);
    }

    private DistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public void calculate(Fare fare) {
        fare.add(Policy.getOverFare(this.distance));
    }

    private enum Policy {
        STANDARD(distance -> distance <= 10, 0),
        NORMAL(distance -> distance > 10 && distance <= 50, 5),
        LONGEST(distance -> distance > 50, 8),
        ;

        private final Predicate<Integer> predicate;
        private final int criteria;
        private static final int NOT_EXISTS_OVER_FARE = 0;

        Policy(Predicate<Integer> predicate, int criteria) {
            this.predicate = predicate;
            this.criteria = criteria;
        }

        private static int getOverFare(final int distance) {
            Policy policy = findType(distance);
            return isStandard(distance) ? NOT_EXISTS_OVER_FARE : calculateOverFare(distance, policy.criteria);
        }

        private static boolean isStandard(int distance) {
            return findType(distance).equals(STANDARD);
        }

        private static int calculateOverFare(final int distance, final int criteria) {
            return (int) ((Math.ceil((distance - 11) / criteria) + 1) * 100);
        }

        private static Policy findType(int distance) {
            return Arrays.stream(Policy.values())
                    .filter(fare -> fare.match(distance))
                    .findFirst()
                    .orElse(STANDARD);
        }

        private boolean match(int distance) {
            return predicate.test(distance);
        }
    }
}
