package support.ticket;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum TicketType {

    CHILD(age -> 6 <= age && age < 13, 50, 350),
    TEENAGER(age -> 13 <= age && age < 19, 20, 350),
    STANDARD(age -> 6 > age || age >= 19, 0, 0);

    private final IntPredicate intPredicate;
    private final int discountRate;
    private final int fixDiscountFare;

    TicketType(IntPredicate intPredicate, int discountRate, int fixDiscountFare) {
        this.intPredicate = intPredicate;
        this.discountRate = discountRate;
        this.fixDiscountFare = fixDiscountFare;
    }

    public static TicketType of(int age) {
        return Arrays.stream(values())
                .filter(ticketType -> ticketType.intPredicate.test(age))
                .findFirst()
                .orElse(TicketType.STANDARD);
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int getFixDiscountFare() {
        return fixDiscountFare;
    }

    public int calculateDiscountedFare(int fare) {
        return (int) ((fare - this.getFixDiscountFare()) * (1 - (float) this.getDiscountRate() / 100));
    }
}
