package nextstep.subway.path.application;

import nextstep.subway.path.domain.DiscountCondition;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.valueobject.Age;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;
import nextstep.subway.path.dto.FareRequest;
import nextstep.subway.path.dto.FareResponse;
import org.springframework.stereotype.Service;

@Service
public class FareService {
    private FareCalculator fareCalculator;
    private PathService pathService;

    public FareService(PathService pathService) {
        this.pathService = pathService;
        this.fareCalculator = new FareCalculator();
    }

    public FareResponse calculate(FareRequest fareRequest) {
        final PathResult pathResult = pathService.findPath(fareRequest.getSource(), fareRequest.getTarget(), fareRequest.getType());
        final Fare totalFare = fareCalculator.calculate(Fare.of(pathResult.getLineMaxFare()),
                Distance.of(pathResult.getTotalDistance()),
                new DiscountCondition(Age.of(fareRequest.getAge())));
        return FareResponse.of(pathResult, Fare.parseInt(totalFare));
    }
}
