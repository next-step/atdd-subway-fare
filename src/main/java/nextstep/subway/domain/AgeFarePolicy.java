package nextstep.subway.domain;

public class AgeFarePolicy implements FarePolicy {

    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private final int age;

    public AgeFarePolicy(int age) {
        this.age = age;
    }

    @Override
    public int calcFare(int currentFare) {
        if (isChild(age)) {
            return (currentFare - DEFAULT_DISCOUNT_FARE) / 2;
        }

        if (isYouth(age)) {
            return (int) Math.floor((currentFare - DEFAULT_DISCOUNT_FARE) * 0.8);
        }

        return currentFare;
    }

    private boolean isYouth(int age) {
        return 13 <= age && age < 19;
    }

    private boolean isChild(int age) {
        return 6 <= age && age < 13;
    }

}
