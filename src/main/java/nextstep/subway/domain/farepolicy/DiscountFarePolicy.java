package nextstep.subway.domain.farepolicy;

public class DiscountFarePolicy implements Policy {

    int AGE_CHILD_MIN = 6;
    int AGE_YOUTH_MIN = 13;
    int AGE_ADULT_MIN = 19;
    int DEDUCTION_FARE = 350;

    private final int age;

    public DiscountFarePolicy(int age) {
        this.age = age;
    }

    @Override
    public int calculate(int fare) {
        return discountFareByAge(fare, this.age);
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
