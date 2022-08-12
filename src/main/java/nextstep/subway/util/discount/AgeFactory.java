package nextstep.subway.util.discount;

public class AgeFactory {

    public static DiscountAgePolicy findUsersAge(Integer age) {
        if (isChildren(age)) {
            return new Children();
        }

        if (isTeenager(age)) {
            return new Teenager();
        }

        return new Adult();
    }

    private static boolean isChildren(Integer age) {
        return age >= 6 && age < 13;
    }

    private static boolean isTeenager(Integer age) {
        return age >= 13 && age < 19;
    }
}
