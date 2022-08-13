package nextstep.subway.domain.policy;

import java.util.List;

public class FareManager {

    private static final List<FarePolicy> policies;

    static {
        policies = List.of(new DefaultFare(),
                new ElevenToFiftyExtraFare());
    }

    private FareManager() {
    }

    public static int fare(int distance) {
        return policies.stream().parallel()
                .filter(farePolicy -> farePolicy.supports(distance))
                .mapToInt(farePolicy -> farePolicy.fare(distance))
                .sum();
    }
}
