package nextstep.subway.domain;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FareCalculatorImpl implements FareCalculator {
    private static final String DISTANCE_OVER = "distanceOver";
    private static final String DISTANCE_UNDER = "distanceUnder";
    private static final String FARE = "fare";
    private static final String STEP = "step";

    private static final int DEFAULT_FARE = 1250;

    private final List<Map<String, Integer>> fareTable;

    public FareCalculatorImpl() {
        this(List.of(
            Map.of(DISTANCE_OVER, 10, DISTANCE_UNDER, 50, STEP, 5, FARE, 100),
            Map.of( DISTANCE_OVER, 50, DISTANCE_UNDER, Integer.MAX_VALUE, STEP, 8, FARE, 100)
        ));
    }

    public FareCalculatorImpl(List<Map<String, Integer>> fareTable) {
        this.fareTable = fareTable;
    }

    public int calculateFare(int distance) {
        int fare = DEFAULT_FARE;
        for (Map<String, Integer> fareMap : fareTable) {
            int distanceToCalculate = distance;
            int distanceOver = fareMap.get(DISTANCE_OVER);
            int distanceUnder = fareMap.get(DISTANCE_UNDER);
            int fareValue = fareMap.get(FARE);
            int step = fareMap.get(STEP);

            if (distanceToCalculate < distanceOver) {
                continue;
            }

            if (distanceToCalculate > distanceUnder) {
                distanceToCalculate = distanceUnder;
            }

            fare += calculateOverFare(distanceToCalculate - distanceOver, step, fareValue);
        }

        return fare;
    }

    private static int calculateOverFare(int distance, int step, int fare) {
        if(distance < 1) {
            return 0;
        }

        return (int) Math.ceil((double) distance / step) * fare;
    }
}
