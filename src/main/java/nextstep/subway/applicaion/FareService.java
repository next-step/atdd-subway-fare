package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.discount.DiscountPolicy;
import nextstep.subway.domain.fare.FareCalculatorChain;
import org.springframework.stereotype.Service;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

import java.util.List;
import java.util.Objects;

@Service
public class FareService {

    private final FareCalculatorChain fareCalculatorChain;
    private final MemberService memberService;
    private final List<DiscountPolicy> discountPolicies;

    public FareService(
            FareCalculatorChain fareCalculator,
            MemberService memberService,
            List<DiscountPolicy> discountPolicies
    ) {
        this.fareCalculatorChain = fareCalculator;
        this.memberService = memberService;
        this.discountPolicies = discountPolicies;
    }

    public int calculate(Path path) {
        int fare = fareCalculatorChain.calculate(path, 0);
        return discount(fare);
    }

    private int discount(int fare) {
        MemberResponse member = requestMember();
        if (Objects.isNull(member)) {
            return fare;
        }

        int result = fare;
        for(DiscountPolicy policy : discountPolicies) {
            if (policy.support(member.getAge())) {
                result = policy.discount(result);
            }
        }
        return result;
    }

    private MemberResponse requestMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }
        return memberService.findMember((String) authentication.getPrincipal());
    }
}
