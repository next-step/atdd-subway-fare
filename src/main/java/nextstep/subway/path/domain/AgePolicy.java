package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public class AgePolicy implements FarePolicy{

    private Policy policy;

    private AgePolicy(int age) {
        this.policy = Policy.choicePolicyByAge(age);
    }

    public static AgePolicy from(int age) {
        return new AgePolicy(age);
    }

    enum Policy {
        TEENAGER(13, 19, (amount) -> (int) ((amount - 350) * 0.8)),
        CHILD(6, 13, (amount) -> (int) ((amount - 350) * 0.5)),
        ADULT(20, Integer.MAX_VALUE, (amount) -> amount),
        NONE(0, 6, (amount) -> amount);;

        private final int minAge;
        private final int maxAge;
        private final Function<Integer, Integer> calculator;

        Policy(int minAge, int maxAge, Function<Integer, Integer> calculator) {
            this.minAge = minAge;
            this.maxAge = maxAge;
            this.calculator = calculator;
        }

        public static Policy choicePolicyByAge(int age) {
            return Arrays.stream(Policy.values())
                    .filter(it -> it.minAge <= age && age < it.maxAge)
                    .findFirst()
                    .orElse(NONE);
        }

        public int calculate(int fare) {
            return calculator.apply(fare);
        }

    }

    @Override
    public Fare getFare(Fare fare) {
        return Fare.from(this.policy.calculate(fare.getValue()));
    }
}
