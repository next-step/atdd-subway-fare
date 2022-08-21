package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.discount.DiscountPolicy;
import nextstep.subway.domain.fare.FarePolicy;
import org.springframework.stereotype.Service;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

import java.util.List;
import java.util.Objects;

@Service
public class FareService {

    private final List<FarePolicy> farePolicies;
    private final List<DiscountPolicy> discountPolicies;
    private final MemberService memberService;

    public FareService(
            List<FarePolicy> farePolicies,
            List<DiscountPolicy> discountPolicies,
            MemberService memberService
    ) {
        this.farePolicies = farePolicies;
        this.discountPolicies = discountPolicies;
        this.memberService = memberService;
    }

    public int calculate(Path path) {
        int fare = farePolicies
                .stream()
                .mapToInt(policy -> policy.fare(path))
                .sum();
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
