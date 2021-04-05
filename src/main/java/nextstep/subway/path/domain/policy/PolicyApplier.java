package nextstep.subway.path.domain.policy;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;

import static nextstep.subway.path.domain.rule.AgePolicyRule.KID;
import static nextstep.subway.path.domain.rule.AgePolicyRule.YOUTH;
import static nextstep.subway.path.domain.rule.DistancePolicyRule.FIRST_RULE;
import static nextstep.subway.path.domain.rule.DistancePolicyRule.SECOND_RULE;

public class PolicyApplier {

    public static Fare applyFarePolicy(PathResult pathResult, LoginMember loginMember){
        Fare fare = new Fare();

        // 기본요금
        fare.addFarePolicy(new BasePolicy());
        // 거리정책
        fare.addFarePolicy(new DistancePolicy(FIRST_RULE, pathResult.getTotalDistance()));
        fare.addFarePolicy(new DistancePolicy(SECOND_RULE, pathResult.getTotalDistance()));
        // 노선정책
        fare.addFarePolicy(new LinePolicy(pathResult.getMaxExtraFare()));
        // 연령정책
        fare.addFarePolicy(new AgePolicy(KID, loginMember.getAge()));
        fare.addFarePolicy(new AgePolicy(YOUTH, loginMember.getAge()));

        return fare;
    }

}
