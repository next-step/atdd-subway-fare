package nextstep.subway.domain;

public class Fare {

    private static final int DEFAULT_LINE_FARE = 0;

    private static final int BASIC_FARE = 1_250;

    private static final int BASIC_FARE_KM = 10;

    private static final int _100WON_PER_5KM_BASED_KM = 50;

    private static final int EXTRA_FARE_BASIC_UNIT = 100;

    private static final int DEDUCTED_FARE = 350;

    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    private int basicFare;

    private int distance;

    private int age;

    public Fare(int distance, int age) {
        this(DEFAULT_LINE_FARE, distance, age);
    }

    public Fare(int lineFare, int distance, int age) {
        this.basicFare = BASIC_FARE + lineFare;
        this.distance = distance;
        this.age = age;
    }

    public int calculate() {

        validateAge();

        int fare = basicFare + calculateOverFare(distance);

        if (isChildren()) {
            return (int) ((fare - DEDUCTED_FARE) * CHILDREN_DISCOUNT_RATE);
        }
        if (isTeenager()) {
            return (int) ((fare - DEDUCTED_FARE) * TEENAGER_DISCOUNT_RATE);
        }

        return fare;
    }

    private void validateAge() {
        if (age <= 0) {
            throw new IllegalArgumentException("잘못된 나이로 요금을 계산할 수 없습니다.");
        }
    }


    private int calculateOverFare(int distance) {
        if (distance <= BASIC_FARE_KM) {
            return 0;
        }

        if (distance <= _100WON_PER_5KM_BASED_KM) {
            return calculateOverFare(BASIC_FARE_KM, distance, 5);
        }

        int extraFareUnder50Km = calculateOverFare(BASIC_FARE_KM, _100WON_PER_5KM_BASED_KM, 5);
        int extraFareOver50Km = calculateOverFare(_100WON_PER_5KM_BASED_KM, distance, 8);

        return extraFareUnder50Km + extraFareOver50Km;

    }

    private int calculateOverFare(int from, int to, int divisor) {
        return (int) ((Math.ceil((to - from - 1) / divisor) + 1) * EXTRA_FARE_BASIC_UNIT);
    }

    private boolean isChildren() {
        return age >= 6 && age < 13;
    }

    private boolean isTeenager() {
        return age >= 13 && age < 19;
    }


}
