package nextstep.subway.util;

public class Teenager extends Age {

    private static final double DISCOUNT_RATE = 0.2;

    @Override
    int discount(int fare) {
        return (int) (fare - ((fare - DEDUCTION_PRICE) * DISCOUNT_RATE));
    }
}
