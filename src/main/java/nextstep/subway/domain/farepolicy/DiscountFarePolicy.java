package nextstep.subway.domain.farepolicy;

import java.util.function.Supplier;

public class DiscountFarePolicy implements Policy {

    private final int age;

    public DiscountFarePolicy(int age) {
        this.age = age;
    }

    Supplier<Integer> expression;

    @Override
    public int calculate(int fare) {
        expression = () -> (discountFareByAge(fare, this.age));

        return expression.get();
    }

    private int discountFareByAge(int fare, int age) {
        if (age < AGE_CHILD_MIN) {
            return 0;
        }

        if (age < AGE_YOUTH_MIN) {
            return (fare - DEDUCTION_FARE) / 10 * 5;
        }

        if (age < AGE_ADULT_MIN) {
            return (fare - DEDUCTION_FARE) / 10 * 8;
        }

        return fare;
    }
}
