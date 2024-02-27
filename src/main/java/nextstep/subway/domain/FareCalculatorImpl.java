package nextstep.subway.domain;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FareCalculatorImpl implements FareCalculator {
    private static final String DISTANCE_OVER = "distanceOver";
    private static final String DISTANCE_UNDER = "distanceUnder";
    private static final String FARE = "fare";

    private final List<Map<String, Integer>> fareTable;

    public FareCalculatorImpl() {
        this(List.of(
            Map.of(DISTANCE_OVER, 0, DISTANCE_UNDER, 10, FARE, 1250),
            Map.of(DISTANCE_OVER, 10, DISTANCE_UNDER, 50, FARE, 100),
            Map.of( DISTANCE_OVER, 50, DISTANCE_UNDER, Integer.MAX_VALUE , FARE, 100)
        ));
    }

    public FareCalculatorImpl(List<Map<String, Integer>> fareTable) {
        this.fareTable = fareTable;
    }

    public int calculateFare(int distance) {
        for (Map<String, Integer> fareMap : fareTable) {
            Integer distanceOver = fareMap.get(DISTANCE_OVER);
            Integer distanceUnder = fareMap.get(DISTANCE_UNDER);
            Integer fare = fareMap.get(FARE);

            if (distance < distanceUnder || distance > distanceOver) {
                continue;
            }

            return calculateOverFare(distance, distanceUnder, fare);
        }

        throw new IllegalArgumentException("요금을 계산할 수 없습니다.");
    }

    private static int calculateOverFare(int distance, int step, int fare) {
        return (int) (Math.ceil((double) (distance - 1) / step) + 1) * fare;
    }
}
