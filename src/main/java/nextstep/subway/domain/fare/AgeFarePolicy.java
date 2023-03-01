package nextstep.subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class AgeFarePolicy {
    private static final int UNDER_TEENAGER_DISCOUNT_AMOUNT = 350;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    public int discountUnderTeenager(final int age, final int fare) {
        if (isTeenager(age)) {
            return discountFare(fare, TEENAGER_DISCOUNT_RATE);
        }

        if (isChild(age)) {
            return discountFare(fare, CHILD_DISCOUNT_RATE);
        }

        return fare;
    }

    private static int discountFare(final int fare, final double discountRate) {
        return (int) ((fare - UNDER_TEENAGER_DISCOUNT_AMOUNT) * (1.0 - discountRate));
    }

    private boolean isTeenager(final Integer age) {
        return 13 <= age && age < 19;
    }

    private boolean isChild(final Integer age) {
        return 6 <= age && age < 13;
    }

}
