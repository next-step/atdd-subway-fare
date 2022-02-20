package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public class ExtraChargePolicy implements FarePolicy {

    private ExtraChargePolicy() {
    }

    private static class ExtraChargePolicyHolder {
        private static final ExtraChargePolicy INSTANCE = new ExtraChargePolicy();
    }

    public static ExtraChargePolicy getInstance() {
        return ExtraChargePolicy.ExtraChargePolicyHolder.INSTANCE;
    }

    @Override
    public Fare apply(Fare fare, FareParams fareParams) {
        return fare.extraCharge(fareParams.getExtraCharge());
    }

}
