package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FareCalculator {

    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public FareCalculator() {
        this.farePolicies.add(new DistanceProportionalFarePolicy());
        this.farePolicies.add(new LineExtraFarePolicy());
        this.farePolicies.add(new DiscountByAgeFarePolicy());
    }

    public Fare calculate(FareContext fareContext) {
        for (FarePolicy farePolicy : this.farePolicies) {
            farePolicy.calculate(fareContext);
        }

        return fareContext.getFare();
    }
}
