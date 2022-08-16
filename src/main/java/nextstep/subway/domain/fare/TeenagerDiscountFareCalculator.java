package nextstep.subway.domain.fare;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.Path;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

import java.util.Objects;

public class TeenagerDiscountFareCalculator extends AbstractFareCalculatorChain {

    private static final int TEENAGER_STARTING_AGE = 13;
    private static final int TEENAGER_ENDS_AGE = 19;
    private static final int ABSOLUTE_DISCOUNT = 350;
    private static final float DISCOUNT_RATIO = 0.2f;

    private final MemberService memberService;

    public TeenagerDiscountFareCalculator(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    protected int convert(Path path, int initialFare) {
        return (int) ((initialFare - ABSOLUTE_DISCOUNT) * (1 - DISCOUNT_RATIO));
    }

    @Override
    public boolean support(Path path) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) {
            return false;
        }

        MemberResponse member = memberService.findMember((String) authentication.getPrincipal());

        return  member.getAge() >= TEENAGER_STARTING_AGE &&
                member.getAge() < TEENAGER_ENDS_AGE;
    }
}
