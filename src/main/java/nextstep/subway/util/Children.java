package nextstep.subway.util;

public class Children extends Age {

    private static final double DISCOUNT_RATE = 0.5;

    @Override
    int discount(int fare) {
        return (int) (fare - ((fare - DEDUCTION_FARE) * DISCOUNT_RATE));
    }
}
