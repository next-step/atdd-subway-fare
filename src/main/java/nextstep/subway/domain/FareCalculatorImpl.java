package nextstep.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FareCalculatorImpl implements FareCalculator {
    private final int DEFAULT_FARE = 1250;
    private final int DEFAULT_OVER_CHARGE_DISTANCE = 5;
    private final int EXTRA_CHARGE_START_DISTANCE = 50;
    private final int EXTRA_CHARGE = 100;
    private final int EXTRA_CHARGE_DISTANCE = 8;
    private final int AGE_CHILD_MIN = 6;
    private final int AGE_YOUTH_MIN = 13;
    private final int AGE_ADULT_MIN = 19;
    private final int DEDUCTION_FARE = 350;

    @Override
    public int calculateFare(int fareDistance, Sections sections, int age) {
        int fare = DEFAULT_FARE + overFare(fareDistance) + maxLineFare(sections);
        return discountFareByAge(fare, age);
    }

    protected int overFare(int distance) {
        if (distance <= EXTRA_CHARGE_START_DISTANCE) {
            return caculateDefaultOverFare(distance);
        }

        int overFare = caculateDefaultOverFare(EXTRA_CHARGE_START_DISTANCE);

        return overFare + caculateExtraFare(distance - EXTRA_CHARGE_START_DISTANCE);
    }

    private int maxLineFare(Sections sections) {
        return sections.getSections().stream()
            .mapToInt(value -> value.getLine().getAdditionalFare())
            .max().getAsInt();
    }

    private int caculateDefaultOverFare(int distance) {
        return (int) ((Math.ceil(((distance) - 1) / DEFAULT_OVER_CHARGE_DISTANCE) + 1) * 100) - 200;
    }

    private int caculateExtraFare(int extraDistance) {
        return ((extraDistance-1) / EXTRA_CHARGE_DISTANCE + 1) * EXTRA_CHARGE;
    }

    public int discountFareByAge(int fare, int age) {
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
