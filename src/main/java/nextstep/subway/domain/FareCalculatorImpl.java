package nextstep.subway.domain;

import java.util.List;
import nextstep.subway.domain.fareOption.Fare10KmTo50KmOption;
import nextstep.subway.domain.fareOption.Fare50KmOverOption;
import nextstep.subway.domain.fareOption.FareCalculateOption;
import org.springframework.stereotype.Component;

@Component
public class FareCalculatorImpl implements FareCalculator {


    private static final int DEFAULT_FARE = 1_250;

    private final List<FareCalculateOption> fareCalculateOptions;

    public FareCalculatorImpl() {
        this(List.of(
            new Fare10KmTo50KmOption(),
            new Fare50KmOverOption()
        ));
    }

    public FareCalculatorImpl(List<FareCalculateOption> fareCalculateOptions) {
        this.fareCalculateOptions = fareCalculateOptions;
    }

    public int calculateFare(int distance) {
        return fareCalculateOptions.stream()
            .filter(calculateOption -> calculateOption.isCalculateTarget(distance))
            .mapToInt(calculateOption -> calculateOverFare(
                calculateOption.getOverDistance(distance),
                calculateOption.getChargingUnitDistance(),
                calculateOption.getFare()
            )).reduce(DEFAULT_FARE, Integer::sum);
    }
}
