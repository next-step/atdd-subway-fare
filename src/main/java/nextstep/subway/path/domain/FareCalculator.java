package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;

import java.util.Arrays;
import java.util.List;

public class FareCalculator {

    private FareCalculator() {
    }

    public static int calculate(FarePolicyRequest request) {
        List<FarePolicy> farePolicies = Arrays.asList(
          DistancePolicy.choicePolicyByDistance(request.getDistance())
        );

        int fare = 0;
        for (FarePolicy farePolicy : farePolicies) {
            fare += farePolicy.calculate(request);
        }
        return fare;
    }
}
