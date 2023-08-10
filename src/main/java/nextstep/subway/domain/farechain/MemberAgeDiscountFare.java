package nextstep.subway.domain.farechain;

public class MemberAgeDiscountFare extends OverFarePolicyHandler {

    private final int memberAge;

    public MemberAgeDiscountFare(OverFarePolicyHandler nextHandler, int memberAge) {
        super(nextHandler);
        this.memberAge = memberAge;
    }

    @Override
    public int chargeOverFare(int fare) {

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
