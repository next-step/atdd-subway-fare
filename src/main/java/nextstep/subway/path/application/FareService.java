package nextstep.subway.path.application;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.FarePolicies;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.policy.AgeDiscountPolicy;
import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.LinePolicy;
import org.springframework.stereotype.Service;

@Service
public class FareService {
    public FarePolicies calculate(PathResult pathResult, LoginMember loginMember) {
        FarePolicy fareDistancePolicy = DistancePolicy.getDistancePolicy(pathResult.getTotalDistance());
        FarePolicy linePolicy = LinePolicy.getLinePolicy(pathResult.getHighSurcharge());
        FarePolicy discountPolicy = AgeDiscountPolicy.getAgeDiscountPolicy(loginMember.getAge());

        return FarePolicies.of(fareDistancePolicy, linePolicy, discountPolicy);
    }
}

