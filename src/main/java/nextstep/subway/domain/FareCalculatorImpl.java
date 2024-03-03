package nextstep.subway.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.fareOption.Fare10KmTo50KmDistanceOption;
import nextstep.subway.domain.fareOption.Fare50KmOverDistanceOption;
import nextstep.subway.domain.fareOption.FareCalculateOption;
import nextstep.subway.domain.fareOption.FareExtraFareDistanceOption;
import org.springframework.stereotype.Component;

@Component
public class FareCalculatorImpl implements FareCalculator {
    private static final int DEFAULT_FARE = 1_250;

    private final List<FareCalculateOption> fareCalculateOptions;

    public FareCalculatorImpl() {
        this(List.of(
            new Fare10KmTo50KmDistanceOption(),
            new Fare50KmOverDistanceOption(),
            new FareExtraFareDistanceOption()
        ));
    }

    public FareCalculatorImpl(List<FareCalculateOption> fareCalculateOptions) {
        this.fareCalculateOptions = fareCalculateOptions;
    }

    public int calculateFare(int distance, Set<LineResponse> line) {
        return fareCalculateOptions.stream()
            .filter(calculateOption -> calculateOption.isCalculateTarget(distance, line))
            .mapToInt(calculateOption -> calculateOption.calculateFare(distance, line))
            .reduce(DEFAULT_FARE, Integer::sum);
    }
}
