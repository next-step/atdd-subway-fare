package nextstep.subway.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FareCalculatorImpl implements FareCalculator {
    private static final String DISTANCE_OVER = "distanceOver";
    private static final String DISTANCE_UNDER = "distanceUnder";
    private static final String FARE = "fare";
    private static final String STEP = "step";

    private static final int DEFAULT_FARE = 1250;

    private final List<FareCalculateOption> fareCalculateOptions;

    public FareCalculatorImpl() {
        this(List.of(
            new FareCalculateOption(10, 50, 5, 100),
            new FareCalculateOption(50, Integer.MAX_VALUE, 8, 100)
        ));
    }

    public FareCalculatorImpl(List<FareCalculateOption> fareCalculateOptions) {
        this.fareCalculateOptions = fareCalculateOptions;
    }

    public int calculateFare(int distance) {
        int fare = DEFAULT_FARE;

        for (FareCalculateOption calculateOption: fareCalculateOptions) {
            if(!calculateOption.isCalculateTarget(distance)) {
                continue;
            }

            fare += calculateOverFare(
                calculateOption.getOverDistance(distance),
                calculateOption.getChargingUnitDistance(),
                calculateOption.getFare()
            );
        }

        return fare;
    }
}
