package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;

import static nextstep.subway.path.domain.policy.PolicyConstant.*;
import static nextstep.subway.path.domain.policy.PolicyConstant.D2_UNIT_DISTANCE;

public class PolicyApplier {

    public static Fare applyFarePolicy(PathResult pathResult){
        Fare fare = new Fare();

        // 거리정책
        fare.addFarePolicy(new DistancePolicy(D1_MIN_DISTANCE.of(), D1_MAX_DISTANCE.of(), D1_UNIT_DISTANCE.of(), pathResult.getTotalDistance()));
        fare.addFarePolicy(new DistancePolicy(D2_MIN_DISTANCE.of(), D2_MAX_DISTANCE.of(), D2_UNIT_DISTANCE.of(), pathResult.getTotalDistance()));
        // 노선정책
        fare.addFarePolicy(new LinePolicy(pathResult.getMaxExtraFare()));

        return fare;
    }

}
