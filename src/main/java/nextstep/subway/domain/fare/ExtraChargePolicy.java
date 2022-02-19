package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public class ExtraChargePolicy implements FarePolicy {

    @Override
    public Fare apply(Fare fare, FareParams fareParams) {
        return fare.extraCharge(fareParams.getExtraCharge());
    }

}
