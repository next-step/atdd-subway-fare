package nextstep.subway.maps.boarding.service;

import nextstep.subway.maps.boarding.domain.Boarding;
import nextstep.subway.maps.boarding.domain.DistanceProportionFareCalculationPolicy;
import nextstep.subway.maps.boarding.domain.FareCalculationPolicyGroup;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.springframework.stereotype.Service;

@Service
public class BoardingService {

    private final FareCalculationPolicyGroup policyGroup;

    public BoardingService(DistanceProportionFareCalculationPolicy distanceProportionFareCalculationPolicy) {
        policyGroup = new FareCalculationPolicyGroup(distanceProportionFareCalculationPolicy);
    }

    public int calculateFare(SubwayPath subwayPath) {
        final Boarding boarding = new Boarding(subwayPath);
        return policyGroup.calculateFare(boarding);
    }
}
