package nextstep.subway.domain.path.fee;

import java.util.function.Function;

public enum AgeType {
    YOUTH (fare -> fare.minus(Fare.deduction()).discount(20)),
    CHILDREN (fare -> fare.minus(Fare.deduction()).discount(50)),
    ANONYMOUS (fare -> fare);

    private final Function<Fare, Fare> function;

    AgeType(final Function<Fare, Fare> function) {
        this.function = function;
    }

    public static AgeType of(Integer age) {
        if (age == null) {
            return ANONYMOUS;
        }

        if (age >= 6 && age < 13) {
            return CHILDREN;
        }

        if (age >= 13 && age < 19) {
            return YOUTH;
        }

        return ANONYMOUS;
    }


    public Fare calculate(final Fare fare) {
        return this.function.apply(fare);
    }
}
