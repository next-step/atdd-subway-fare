package nextstep.subway.applicaion;

import org.springframework.stereotype.Component;

import java.util.function.IntUnaryOperator;

@Component
public class FareCalculator {

    private static final int MINIMUM_DISTANCE = 10;
    private static final int MINIMUM_FARE = 1250;

    private static final int FIFTY = 50;

    private static final double BETWEEN_10_AND_50_DENOMINATOR = 5;
    private static final double OVER_50_DENOMINATOR = 8;

    private static final IntUnaryOperator BETWEEN_10_AND_50 = d -> (int) Math.ceil(d / BETWEEN_10_AND_50_DENOMINATOR) * 100;
    private static final IntUnaryOperator OVER_50 = d -> (int) Math.ceil(d / OVER_50_DENOMINATOR) * 100;

    public int calculateOverFare(int distance, int age) {
        if (distance == 0) {
            return 0;
        }

        final int over50 = Math.max(distance - FIFTY, 0);
        final int between10and50 = Math.max(distance - over50 - MINIMUM_DISTANCE, 0);

        final int price = MINIMUM_FARE + BETWEEN_10_AND_50.applyAsInt(between10and50) + OVER_50.applyAsInt(over50);

        return calculateByAge(age, price);
    }

    private int calculateByAge(int age, int price) {
        if (age < 13 && age >= 6) {
            return (price - 350) / 10 * 5;
        } else if (age < 19 && age >= 13) {
            return (price - 350) / 10 * 8;
        }
        return price;
    }
}
