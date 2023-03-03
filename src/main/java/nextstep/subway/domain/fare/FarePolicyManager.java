package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

@Component
public class FarePolicyManager {
    private final FarePolicyHandler policyChain;

    public FarePolicyManager() {
        DistanceFarePolicyHandler distanceFare = new DistanceFarePolicyHandler();
        LineFarePolicyHandler lineFare = new LineFarePolicyHandler();
        DiscountPolicyHandler discountFare = new DiscountPolicyHandler();

        distanceFare.next(lineFare);
        lineFare.next(discountFare);

        this.policyChain = distanceFare;
    }

    public int calculate(Path path, Member member) {
        return policyChain.apply(Fare.of(path, member)).getCost();
    }
}
