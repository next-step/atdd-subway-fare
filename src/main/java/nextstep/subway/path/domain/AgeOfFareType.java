package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeOfFareType {

    CHILDREN_FARE(350,0.5, age -> age >= 6 && age < 13),
    TEENAGER_FARE(350,0.8, age -> age >= 13 && age < 19),
    ADULT_FARE(0,1.0, age -> age >= 19);

    private int discountFare;
    private double discountRate;
    private Function<Integer, Boolean> ageType;

    AgeOfFareType(int discountFare, double discountRate, Function<Integer, Boolean> ageType) {
        this.discountFare = discountFare;
        this.discountRate = discountRate;
        this.ageType = ageType;
    }

    private static AgeOfFareType valueOf(int age) {
        return Arrays.stream(AgeOfFareType.values())
                .filter(it -> it.isAgeType(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이는 0보다 커야 합니다."));
    }

    public boolean isAgeType(int age) {
        return ageType.apply(age);
    }

    public static int calculate(int fare, int age) {
        AgeOfFareType ageOfFareType = valueOf(age);

        int discountedOverFare = (int) ((fare - ageOfFareType.discountFare) * ageOfFareType.discountRate);
        return discountedOverFare;
    }
}
