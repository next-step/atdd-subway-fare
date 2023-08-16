package nextstep.subway.domain.farechain.discountfare;

import java.util.Optional;

public class MemberAgeDiscountFare extends DiscountFarePolicyHandler {

    private static final int NORMAL_AGE = 30;

    private final int memberAge;

    public MemberAgeDiscountFare(Optional<Integer> memberAge) {
        super(null);
        this.memberAge = memberAge.orElse(NORMAL_AGE);
    }


    @Override
    public int discountOverFare(int fare) {

        if (isTeenager()) {
            return super.chargeHandler(fare - (int) Math.ceil((fare - 350) * (0.2)));
        }

        if (isChildren()) {
            return super.chargeHandler(fare - (int) Math.ceil((fare - 350) * (0.5)));
        }

        return super.chargeHandler(fare);
    }

    private boolean isTeenager() {
        return memberAge >= 13 && memberAge < 19;
    }

    private boolean isChildren() {
        return memberAge >= 6 && memberAge < 13;
    }
}
