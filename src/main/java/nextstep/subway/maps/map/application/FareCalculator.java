package nextstep.subway.maps.map.application;

import nextstep.subway.maps.map.domain.DistanceProportionalFarePolicy;
import nextstep.subway.maps.map.domain.FareContext;
import nextstep.subway.maps.map.domain.FarePolicy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FareCalculator {

    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public FareCalculator() {
        this.farePolicies.add(new DistanceProportionalFarePolicy());
    }

    public int calculate(FareContext fareContext) {
        for (FarePolicy farePolicy : this.farePolicies) {
            farePolicy.calculate(fareContext);
        }

        return fareContext.getFare();
    }
}
