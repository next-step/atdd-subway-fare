package nextstep.path.fare;

import java.util.Objects;

public class DiscountPolicy {

    public Fare discount(Fare fare, Integer age) {
        if (Objects.isNull(age)) {
            return fare;
        }

        if (isChild(age)) {
            return Fare.of((int) ((fare.getValue() - 350) * 0.5));
        }

        if (isTeenager(age)) {
            return Fare.of((int) ((fare.getValue() - 350) * 0.8));
        }

        return fare;
    }

    private boolean isChild(int age) {
        return 6 <= age && age < 13;
    }

    private boolean isTeenager(int age) {
        return 13 <= age && age < 19;
    }
}
