package nextstep.subway.domain.fare;

import java.util.List;

public class FarePolicies {

    private static final List<FarePolicy> farePolicies = List.of(
            new FirstFarePolicy(),
            new BetweenFarePolicy(10, 50, 5),
            new LastFarePolicy(50, 8)
    );

    public static int getFare(final int distance) {
        return farePolicies.stream()
                .mapToInt(farePolicy -> farePolicy.getFare(distance))
                .sum();
    }
}
