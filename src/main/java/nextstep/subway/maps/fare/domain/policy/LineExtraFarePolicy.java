package nextstep.subway.maps.fare.domain.policy;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;

public class LineExtraFarePolicy implements FarePolicy {
    @Override
    public void calculate(FareContext fareContext) {
        int extraFare = fareContext.getExtraFare();

        Fare fare = fareContext.getFare();
        fare.plusFare(extraFare);
    }
}
