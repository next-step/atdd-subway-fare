package support.ticket;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.IntPredicate;

import static nextstep.utils.NumberUtils.requirePositiveNumber;

@RequiredArgsConstructor
public enum TicketType {

    CHILD(age -> Ages.A6 <= age && age < Ages.A13, Rates.R50),
    TEENAGER(age -> Ages.A13 <= age && age < Ages.A19, Rates.R20),
    STANDARD(age -> Ages.A6 > age || age > Ages.A19, Rates.ZERO);

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
        private static final int A6 = 6;
        private static final int A13 = 13;
        private static final int A19 = 19;
    }

    private static class Fares {
        private static final int FIX_DEDUCTION = 350;
    }

    private static class Rates {

        private static final int ZERO = 0;
        private static final int R20 = 20;
        private static final int R50 = 50;
    }
}
