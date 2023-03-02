package nextstep.subway.domain.fare;

import java.util.stream.Stream;

public class DistanceFarePolicyHandler extends FarePolicyHandler {
    public static final int DEFAULT_FARE = 1_250;

    @Override
    public Fare execute(Fare fare) {
        int distance = fare.getPath().distance();

        int overFare = Stream.of(DistanceFarePolicy.values())
                .filter(policy -> policy.supported(distance))
                .mapToInt(policy -> policy.additionalFare(distance))
                .sum();

        return fare.withModified(DEFAULT_FARE + overFare);
    }
}
