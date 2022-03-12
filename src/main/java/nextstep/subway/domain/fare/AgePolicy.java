package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public class AgePolicy implements FarePolicy {

    private final int age;

    public static AgePolicy from(int age) {
        return new AgePolicy(age);
    }

    private AgePolicy(int age) {
        this.age = age;
    }

    @Override
    public void calculate(Fare fare) {
        fare.change(Policy.calculate(age, fare.getFare()));
    }

    private enum Policy {
        BABY(age -> age < 6, 0, 0.0),
        CHILDREN(age -> age >= 6 && age < 13, 350,0.2),
        TEENAGER(age -> age >= 13 && age < 19, 350, 0.5),
        ADULT(age -> age > 19, 0, 0.0),
        ;

        private final Predicate<Integer> predicate;
        private final int deductionMoney;
        private final double discountRate;

        Policy(final Predicate<Integer> predicate, final int deductionMoney, final double discountRate) {
            this.predicate = predicate;
            this.deductionMoney = deductionMoney;
            this.discountRate = discountRate;
        }

        private static int calculate(final int age, final int fare) {
            Policy policy = Policy.findType(age);
            int discountMoney = (int) ((fare - policy.deductionMoney) * policy.discountRate);
            return fare - discountMoney;
        }

        private static Policy findType(final int age) {
            return Arrays.stream(Policy.values())
                    .filter(policy -> policy.match(age))
                    .findFirst()
                    .orElse(BABY);
        }

        private boolean match(final int age) {
            return predicate.test(age);
        }
    }
}
