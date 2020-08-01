package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.DistanceProportionalFarePolicy;
import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;
import nextstep.subway.maps.fare.domain.FarePolicy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FareCalculator {

    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public FareCalculator() {
        this.farePolicies.add(new DistanceProportionalFarePolicy());
    }

    public Fare calculate(FareContext fareContext) {
        for (FarePolicy farePolicy : this.farePolicies) {
            farePolicy.calculate(fareContext);
        }

        return fareContext.getFare();
    }
}
