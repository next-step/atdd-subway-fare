package support.ticket;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.IntPredicate;

import static nextstep.utils.NumberUtils.requirePositiveNumber;

@RequiredArgsConstructor
public enum TicketType {

    CHILD(age -> Ages.START_OF_CHILD <= age && age < Ages.START_OF_TEENAGER, Rates.CHILD_DISCOUNT),
    TEENAGER(age -> Ages.START_OF_TEENAGER <= age && age < Ages.START_OF_ADULT, Rates.TEENAGER_DISCOUNT),
    STANDARD(age -> Ages.START_OF_CHILD > age || age > Ages.START_OF_ADULT, Rates.STANDARD_DISCOUNT);

    private final IntPredicate predicate;
    private final int discountRate;

    public static TicketType of(int age) {
        requirePositiveNumber(age);

        return Arrays.stream(values())
                .filter(b -> b.predicate.test(age))
                .findFirst()
                .orElse(TicketType.STANDARD);
    }

    public int applyFareDiscount(int originalFare) {
        if (this == STANDARD) {
            return originalFare;
        }

        int fixDeductionFare = discountFixDeduction(originalFare);
        return (int) (fixDeductionFare - ((fixDeductionFare) * discountRate * 0.01));

    }

    private static int discountFixDeduction(int fare) {
        return fare - Fares.FIX_DEDUCTION;
    }

    private static class Ages {
        private static final int START_OF_CHILD = 6;
        private static final int START_OF_TEENAGER = 13;
        private static final int START_OF_ADULT = 19;
    }

    private static class Fares {
        private static final int FIX_DEDUCTION = 350;
    }

    private static class Rates {

        private static final int STANDARD_DISCOUNT = 0;
        private static final int TEENAGER_DISCOUNT = 20;
        private static final int CHILD_DISCOUNT = 50;
    }
}
