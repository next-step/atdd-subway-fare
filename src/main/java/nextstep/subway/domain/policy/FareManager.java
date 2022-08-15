package nextstep.subway.domain.policy;

import java.util.ArrayList;
import java.util.List;

public class FareManager {

    private static final List<FarePolicy> policies = new ArrayList<>();

    private FareManager() {
    }

    public static void addPolicy(FarePolicy farePolicy) {
        policies.add(farePolicy);
    }

    public static void clearPolicy() {
        if (policies.size() > 0) {
            policies.clear();
        }
    }

    public static int fare(PathByFare pathByFare) {
        return policies.stream().parallel()
                .filter(farePolicy -> farePolicy.supports(pathByFare))
                .mapToInt(farePolicy -> farePolicy.fare(pathByFare))
                .sum();
    }
}
