package nextstep.subway.path.application;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.enumeration.FareDistanceType;
import org.springframework.stereotype.Service;

@Service
public class DistanceFareService implements FarePolicy {

    private FareDistanceType distanceType;

    private DistanceFareService() {}

    private DistanceFareService(FareDistanceType distanceType) {
        this.distanceType = distanceType;
    }

    public DistanceFareService(int distance) {
        new DistanceFareService(FareDistanceType.typeFromDistance(distance));
    }

    @Override
    public Fare calculate() {
        //return this.distanceType.calucate();
        return null;
    }
}
