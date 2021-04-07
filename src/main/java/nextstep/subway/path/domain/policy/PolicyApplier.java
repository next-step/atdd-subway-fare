package nextstep.subway.path.domain.policy;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import org.springframework.stereotype.Component;

import static nextstep.subway.path.domain.rule.AgePolicyRule.KID;
import static nextstep.subway.path.domain.rule.AgePolicyRule.YOUTH;
import static nextstep.subway.path.domain.rule.DistancePolicyRule.FIRST_RULE;
import static nextstep.subway.path.domain.rule.DistancePolicyRule.SECOND_RULE;

@Component
public class PolicyApplier {

    private FarePolices farePolices;

    public Fare applyFarePolicy(PathResult pathResult, LoginMember loginMember){
        farePolices = new FarePolices();

        // 기본요금
        farePolices.addFarePolicy(new BasePolicy());
        // 거리정책
        farePolices.addFarePolicy(new DistancePolicy(FIRST_RULE, pathResult.getTotalDistance()));
        farePolices.addFarePolicy(new DistancePolicy(SECOND_RULE, pathResult.getTotalDistance()));
        // 노선정책
        farePolices.addFarePolicy(new LinePolicy(pathResult.getMaxExtraFare()));
        // 연령정책
        farePolices.addFarePolicy(new AgePolicy(KID, loginMember.getAge()));
        farePolices.addFarePolicy(new AgePolicy(YOUTH, loginMember.getAge()));

        return new Fare(farePolices.calculateFare());
    }

}
