package nextstep.subway.util;

public class DiscountAgeFactory {

    public static DiscountAgePolicy findUsersAge(Integer age) {
        if (isChildren(age)) {
            return new DiscountChildren();
        }

        if (isTeenager(age)) {
            return new DiscountTeenager();
        }

        return new DiscountAdult();
    }

    private static boolean isChildren(Integer age) {
        return age >= 6 && age < 13;
    }

    private static boolean isTeenager(Integer age) {
        return age >= 13 && age < 19;
    }
}
