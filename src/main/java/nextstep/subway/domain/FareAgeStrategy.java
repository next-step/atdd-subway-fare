package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class FareAgeStrategy {

    private static BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public Fare calculate(Integer age, Fare fare) {
        BigDecimal deduct = DiscountAgeRange.getDeduct(age);
        int discountPercent = DiscountAgeRange.getDiscountPercent(age);

        BigDecimal origin = fare.getFare();
        BigDecimal subtract = origin.subtract(deduct);
        BigDecimal discountFare = subtract
                .multiply(BigDecimal.valueOf(discountPercent))
                .divide(ONE_HUNDRED);

        return Fare.of(subtract.subtract(discountFare).add(deduct));
    }

    private enum DiscountAgeRange {

        KIDS(6, 13, BigDecimal.valueOf(350), 50),
        TEEN(13, 19, BigDecimal.valueOf(350), 20);

        private static final int DEFAULT_DISCOUNT_PERCENT = 0;
        private static final BigDecimal DEFAULT_DEDUCT = BigDecimal.ZERO;

        private int startAge;
        private int endAge;
        private BigDecimal deduct;
        private int discountPercent;

        DiscountAgeRange(int startAge, int endAge, BigDecimal deduct, int discountPercent) {
            this.startAge = startAge;
            this.endAge = endAge;
            this.deduct = deduct;
            this.discountPercent = discountPercent;
        }

        public static int getDiscountPercent(Integer age) {
            if (Objects.isNull(age)) {
                return DEFAULT_DISCOUNT_PERCENT;
            }

            if (isKids(age)) {
                return KIDS.discountPercent;
            }

            return TEEN.discountPercent;
        }

        public static BigDecimal getDeduct(Integer age) {
            if (Objects.isNull(age)) {
                return DEFAULT_DEDUCT;
            }

            if (isKids(age)) {
                return KIDS.deduct;
            }

            return TEEN.deduct;
        }

        private static boolean isKids(int age) {
            return age >= KIDS.startAge && age < KIDS.endAge;
        }

    }

}
