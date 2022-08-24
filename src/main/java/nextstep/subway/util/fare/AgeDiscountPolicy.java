package nextstep.subway.util.fare;

public class AgeDiscountPolicy {
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    private static final int CHILDREN_AGE_MIN = 6;
    private static final int CHILDREN_AGE_MAX = 12;
    private static final int TEENAGE_AGE_MIN = 13;
    private static final int TEENAGE_AGE_MAX = 18;

    private static final int EXCLUDE_FARE = 350;

    public static int calculate(int fare, int age) {
        if (isTeenager(age)) {
            return (int) ((fare - EXCLUDE_FARE) * CHILDREN_DISCOUNT_RATE);
        }

        if (isChildren(age)) {
            return (int) ((fare - EXCLUDE_FARE) * TEENAGER_DISCOUNT_RATE);
        }

        return 0;
    }

    private static boolean isTeenager(int age) {
        return TEENAGE_AGE_MIN <= age && age <= TEENAGE_AGE_MAX;
    }

    private static boolean isChildren(int age) {
        return CHILDREN_AGE_MIN <= age && age <= CHILDREN_AGE_MAX;
    }
}
