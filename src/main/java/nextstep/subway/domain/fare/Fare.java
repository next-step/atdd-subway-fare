package nextstep.subway.domain.fare;

import java.util.ArrayList;
import java.util.List;

public class Fare {
    private static final int BASIC_FARE = 1_250;

    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public int calculateFare() {
        int fare = BASIC_FARE;

        for (FarePolicy farePolicy : farePolicies) {
            fare = farePolicy.calculateOverFare(fare);
        }

        return fare;
    }

    public void addPolicy(FarePolicy farePolicy) {
        farePolicies.add(farePolicy);
    }
}
