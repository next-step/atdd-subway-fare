package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class DiscountFareCalculator {

    private final DiscountFare discountFare;

    public DiscountFareCalculator(LoginMember loginMember) {
        discountFare = DiscountFare.valueOf(loginMember);
    }

    public int calculate(int fare) {
        return discountFare.applyDiscount(fare);
    }

    private enum DiscountFare {
        BABY(age -> age < 6, 1350, 1),
        YOUTH(age -> age < 13, 350, 0.2),
        CHILD(age -> age < 19, 350, 0.5),
        ADULT(age -> age >= 19, 0, 0);

        private final Function<Integer, Boolean> condition;
        private final int discountFare;
        private final double discountRateAboutTotal;

        DiscountFare(Function<Integer, Boolean> condition, int discountFare, double discountRateAboutTotal) {
            this.condition = condition;
            this.discountFare = discountFare;
            this.discountRateAboutTotal = discountRateAboutTotal;
        }

        public static DiscountFare valueOf(LoginMember loginMember) {
            return valueOf(loginMember.getAge());
        }

        public static DiscountFare valueOf(int age) {
            return Arrays.stream(values())
                .filter(it -> it.condition.apply(age))
                .findFirst()
                .orElse(ADULT);
        }

        public int applyDiscount(int fare) {
            return (int)((fare - discountFare) * discountRateAboutTotal);
        }
    }
}
