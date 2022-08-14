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

    public static int fare(int distance) {
        return policies.stream().parallel()
                .filter(farePolicy -> farePolicy.supports(distance))
                .mapToInt(farePolicy -> farePolicy.fare(distance))
                .sum();
    }
}
