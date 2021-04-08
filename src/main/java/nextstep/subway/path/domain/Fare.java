package nextstep.subway.path.domain;

import java.util.List;

public class Fare {
    private static final int BASE_FARE = 1250;

    public int calculateFare(List<FarePolicy> farePolicies) {
        if(farePolicies.isEmpty()) {
            return BASE_FARE;
        }

        int total = BASE_FARE;

        for(FarePolicy farePolicy : farePolicies) {
            total = farePolicy.calculateFareByPolicy(total);
        }

        return total;
    }
}
