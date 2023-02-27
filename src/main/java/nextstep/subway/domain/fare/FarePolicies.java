package nextstep.subway.domain.fare;

import java.util.List;

public class FarePolicies {

    private static final List<FarePolicy> farePolicies = List.of(
            new DefaultFarePolicy(),
            new BetweenTenAndFiftyFarePolicy(),
            new OverFiftyFarePolicy()
    );

    public static int getFare(final int distance) {
        return farePolicies.stream()
                .mapToInt(farePolicy -> farePolicy.getFare(distance))
                .sum();
    }
}
