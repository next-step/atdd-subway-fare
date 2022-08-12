package nextstep.subway.util.discount;

public class AgeFactory {

    public static DiscountAgePolicy findUsersAge(Integer age) {
        if (Children.support(age)) {
            return new Children();
        }

        if (Teenager.support(age)) {
            return new Teenager();
        }

        return new Adult();
    }
}
