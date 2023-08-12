package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Optional;

public class DiscountFareByAge extends FarePolicy {

    private static final BigDecimal discountDeduction = new BigDecimal(350);
    private static final int START_AGE_FOR_CHILD_FARE = 6;
    private static final int START_AGE_FOR_TEEN_FARE = 13;
    private static final int START_AGE_FOR_NORMAL_FARE = 19;

    private final Optional<Integer> ageOptional;

    public DiscountFareByAge(Optional<Integer> age) {
        this.ageOptional = age;
    }

    /**
     * - 청소년: 13세 이상~19세 미만 20%
     * - 어린이: 6세 이상~ 13세 미만 50%
     * - 유아: 6세 미만 무료
     */
    @Override
    public int fare(int prevFare) {

        if (ageOptional.isEmpty()) {
            return prevFare;
        }

        int age = ageOptional.get();

        BigDecimal fare = new BigDecimal(prevFare);

        if (age < START_AGE_FOR_CHILD_FARE) {
            return 0;
        }

        if (age < START_AGE_FOR_TEEN_FARE) {
            return fare.subtract(discountDeduction).multiply(new BigDecimal("0.5")).add(discountDeduction).intValue();
        }

        if (age < START_AGE_FOR_NORMAL_FARE) {
            return fare.subtract(discountDeduction).multiply(new BigDecimal("0.8")).add(discountDeduction).intValue();
        }

        return fare.intValue();
    }
}
