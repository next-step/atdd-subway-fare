package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.discount.DiscountPolicy;
import nextstep.subway.applicaion.discount.NotDiscountPolicy;
import support.auth.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final List<DiscountPolicy> discountPolicyList;

    public DiscountPolicy findDiscountPolicy(final Member member) {
        return discountPolicyList.stream()
                .filter(policy -> policy.isTarget(member))
                .findFirst()
                .orElse(new NotDiscountPolicy());
    }

}
