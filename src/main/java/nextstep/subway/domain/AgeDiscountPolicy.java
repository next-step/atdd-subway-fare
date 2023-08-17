package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.BiFunction;

public class AgeDiscountPolicy extends FarePolicy {
    private static final int BASE_DISCOUNT_FARE = 350;

    private final int age;

    AgeDiscountPolicy(int age) {
        this.age = age;
    }

    @Override
    protected int calculateFare(int fare) {
        return AgeDiscount.calculate(this.age, fare);
    }

    private enum AgeDiscount {
        ADULT(19, 0, AgeDiscountLogic.REMAIN),
        YOUTH(13, 20, AgeDiscountLogic.DISCOUNT),
        KID(6, 50, AgeDiscountLogic.DISCOUNT);

        private final int minAge;
        private final int discountRate;
        private final AgeDiscountLogic logic;

        AgeDiscount(int minAge, int discountRate, AgeDiscountLogic logic) {
            this.minAge = minAge;
            this.discountRate = discountRate;
            this.logic = logic;
        }

        static int calculate(int age, int fare) {
            AgeDiscount discount = Arrays.stream(values())
                .filter(ageDiscount -> ageDiscount.minAge <= age)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

            return discount.logic.calculate(fare, discount.discountRate);
        }
    }

    private enum AgeDiscountLogic {
        DISCOUNT((fare, discountRate) -> (int) Math.round((fare - BASE_DISCOUNT_FARE) * ((double) (100 - discountRate) / 100))),
        REMAIN((fare, discountRate) -> fare);

        private final BiFunction<Integer, Integer, Integer> calculator;

        AgeDiscountLogic(BiFunction<Integer, Integer, Integer> calculator) {
            this.calculator = calculator;
        }

        private int calculate(int fare, int discountRate) {
            return this.calculator.apply(fare, discountRate);
        }
    }
}
