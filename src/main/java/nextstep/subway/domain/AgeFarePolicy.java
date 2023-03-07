package nextstep.subway.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public enum AgeFarePolicy {
    CHILDREN(
            age -> 6 <= age && age < 13,
            fare -> discount(fare, 50)
    ),
    TEENAGER(
            age -> 13 <= age && age < 19,
            fare -> discount(fare, 20)
    )
    ;
    private final IntPredicate ageRange;
    private final IntUnaryOperator discountOperator;

    AgeFarePolicy(IntPredicate ageRange, IntUnaryOperator discountOperator) {
        this.ageRange = ageRange;
        this.discountOperator = discountOperator;
    }

    public static int calculate(int fare, int age) {
        Optional<AgeFarePolicy> ageFarePolicy = findPolicy(age);

        if (ageFarePolicy.isEmpty()) {
            return fare;
        }
        return ageFarePolicy.get()
                .discountOperator.applyAsInt(fare);
    }

    private static Optional<AgeFarePolicy> findPolicy(int age) {
        Optional<AgeFarePolicy> ageFarePolicy = Arrays.stream(values())
                .filter(it -> it.ageRange.test(age))
                .findFirst();
        return ageFarePolicy;
    }

    private static int discount(int fare, int discountPercentage) {
        double discountedRatio = 1.0 - (discountPercentage / 100.0);
        return (int) (fare * discountedRatio);
    }
}
