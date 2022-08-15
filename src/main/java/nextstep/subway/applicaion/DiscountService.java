package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.discount.DiscountPolicy;
import nextstep.subway.applicaion.discount.NotDiscountPolicy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final List<DiscountPolicy> discountPolicyList;

    @PostConstruct
    void init() {
        discountPolicyList.add(new NotDiscountPolicy());
    }

    public DiscountPolicy findDiscountPolicy(final Member member) {
        return discountPolicyList.stream()
                .filter(policy -> policy.isTarget(member))
                .findFirst()
                .orElse(new NotDiscountPolicy());
    }

}
