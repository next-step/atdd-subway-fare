package nextstep.core.subway.pathFinder.application;

import nextstep.core.subway.line.application.LineService;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderResponse;
import nextstep.core.subway.pathFinder.domain.PathFinderType;
import org.springframework.stereotype.Service;

@Service
public class PathFinderService {

    private final LineService lineService;

    private final PathFinder pathFinder;

    private final FareCalculator fareCalculator;

    public PathFinderService(LineService lineService, PathFinder pathFinder, FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public PathFinderResponse findOptimalPath(PathFinderRequest pathFinderRequest) {
        validatePathRequest(pathFinderRequest);

        PathFinderResponse optimalPath = pathFinder.findOptimalPath(
                lineService.findAllLines(),
                lineService.findStation(pathFinderRequest.getDepartureStationId()),
                lineService.findStation(pathFinderRequest.getArrivalStationId()),
                PathFinderType.findType(pathFinderRequest.getPathFinderType()));

        int fare = fareCalculator.calculateFare(optimalPath.getDistance());
        return PathFinderResponse.setFareInResponse(fare, optimalPath);
    }

    public boolean isValidPath(PathFinderRequest pathFinderRequest) {
        validatePathRequest(pathFinderRequest);

        return pathFinder.existPathBetweenStations(
                lineService.findAllLines(),
                lineService.findStation(pathFinderRequest.getDepartureStationId()),
                lineService.findStation(pathFinderRequest.getArrivalStationId()));
    }

    private void validatePathRequest(PathFinderRequest pathFinderRequest) {
        if (areStationsSame(pathFinderRequest)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일할 수 없습니다.");
        }
    }

    private boolean areStationsSame(PathFinderRequest pathFinderRequest) {
        return pathFinderRequest.getDepartureStationId().equals(pathFinderRequest.getArrivalStationId());
    }
}
