package subway.path.domain;

import subway.path.application.dto.PathFareCalculationInfo;
import subway.path.domain.handler.DistancePathFareHandler;
import subway.path.domain.handler.GraphPathFareHandler;
import subway.path.domain.handler.LineSurchargePathFareHandler;
import subway.path.domain.handler.MemberAgePathFareHandler;
import subway.path.domain.handler.PathFareChain;

public class PathFare {
    private final PathFareChain pathFareChain;

    public PathFare() {
        this.pathFareChain = PathFareChain.chain(new GraphPathFareHandler(),
                new DistancePathFareHandler(),
                new LineSurchargePathFareHandler(),
                new MemberAgePathFareHandler());
    }

    public long calculateFare(PathFareCalculationInfo calcInfo) {
        PathFareCalculationInfo calcInfoResponse = pathFareChain.calculateFare(calcInfo);
        return calcInfoResponse.getFare();
    }
}
