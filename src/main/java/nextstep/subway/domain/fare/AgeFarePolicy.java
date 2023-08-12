package nextstep.subway.domain.fare;

import nextstep.subway.domain.fare.age.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AgeFarePolicy extends FarePolicy {

    private static final BigDecimal DISCOUNT_DEDUCTION = new BigDecimal(350);

    private static final DiscountAge NO_DISCOUNT_AGE = new NoDiscountAge();
    private static final DiscountAge TODDLER_DISCOUNT_AGE = new ToddlerDiscountAge();
    private static final DiscountAge CHILD_DISCOUNT_AGE = new ChildDiscountAge(DISCOUNT_DEDUCTION);
    private static final DiscountAge TEEN_DISCOUNT_AGE = new TeenDiscountAge(DISCOUNT_DEDUCTION);
    private static final List<DiscountAge> DISCOUNT_AGE_LIST = List.of(NO_DISCOUNT_AGE, TODDLER_DISCOUNT_AGE, CHILD_DISCOUNT_AGE, TEEN_DISCOUNT_AGE);


    private final DiscountAge discountAge;

    public AgeFarePolicy(Optional<Integer> age) {

        this.discountAge = age.map(integer -> DISCOUNT_AGE_LIST.stream()
                                           .filter(discountAge -> discountAge.isTarget(integer))
                                           .findFirst()
                                           .orElse(NO_DISCOUNT_AGE)
                ).orElse(NO_DISCOUNT_AGE);
    }

    /**
     * - 청소년: 13세 이상~19세 미만 20%
     * - 어린이: 6세 이상~ 13세 미만 50%
     * - 유아: 6세 미만 무료
     */
    @Override
    public int fare(int prevFare) {
        return discountAge.discount(prevFare);
    }
}
