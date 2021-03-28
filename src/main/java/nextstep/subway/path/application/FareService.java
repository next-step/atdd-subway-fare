package nextstep.subway.path.application;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import org.springframework.stereotype.Service;


@Service
public class FareService {

    public FareService() {
    }

    public Fare calculateFare(PathResult pathResult) {
        return new Fare(pathResult.getTotalDistance());
    }

}
