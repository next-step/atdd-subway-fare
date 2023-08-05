package subway.path.application.fare;

import subway.path.application.dto.PathFareCalculationInfo;

public class PathFare {
    private final PathFareChain pathFareChain;

    public PathFare() {
        this.pathFareChain = PathFareChain.chain(new GraphPathFare(),
                new DistancePathFare(),
                new LineSurchargePathFare(),
                new MemberAgePathFare());
    }

    public long calculateFare(PathFareCalculationInfo calcInfo) {
        PathFareCalculationInfo calcInfoResponse = pathFareChain.calculateFare(calcInfo);
        return calcInfoResponse.getFare();
    }
}
