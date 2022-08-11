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

    public int calculateOverFare(int distance) {
        if (distance == 0) {
            return 0;
        }

        int over50 = Math.max(distance - FIFTY, 0);
        int between10and50 = Math.max(distance - over50 - MINIMUM_DISTANCE, 0);

        return MINIMUM_FARE + BETWEEN_10_AND_50.applyAsInt(between10and50) + OVER_50.applyAsInt(over50);
    }
}
