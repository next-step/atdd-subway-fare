package nextstep.subway.domain.fare;

public class DiscountPolicyHandler extends FarePolicyHandler {

    @Override
    public Fare execute(Fare fare) {
        DiscountPolicy type = DiscountPolicy.of(fare.getMember());
        int appliedCost = type.apply(fare.getCost());

        return fare.withModified(appliedCost);
    }
}
