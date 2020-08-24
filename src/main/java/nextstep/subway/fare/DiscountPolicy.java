package nextstep.subway.fare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DiscountPolicy {
    private List<ExtraFarePolicy> extraFarePolicies = new ArrayList<>();

    public DiscountPolicy(ExtraFarePolicy... extraFarePolicies) {
        this.extraFarePolicies = Arrays.asList(extraFarePolicies);
    }

    public int calculateDiscountAmount(int fare, int extraFare) {
        int result = fare;
        for (ExtraFarePolicy each : extraFarePolicies) {
            result = getDiscountAmount(fare + each.addExtraFee(extraFare));
        }
        return result;
    }

    protected abstract int getDiscountAmount(int fare);
}
