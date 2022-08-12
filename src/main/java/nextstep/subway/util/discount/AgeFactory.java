package nextstep.subway.util.discount;

import org.springframework.stereotype.Component;

@Component
public class AgeFactory {

    public DiscountAgePolicy findUsersAge(Integer age) {
        if (Children.support(age)) {
            return new Children();
        }

        if (Teenager.support(age)) {
            return new Teenager();
        }

        return new Adult();
    }
}
