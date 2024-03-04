package nextstep.core.subway.pathFinder.application;

import nextstep.core.subway.line.application.LineService;
import nextstep.core.subway.pathFinder.application.converter.PathFinderConverter;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.pathFinder.application.dto.PathFinderResponse;
import nextstep.core.subway.pathFinder.domain.PathFinderType;
import nextstep.core.subway.pathFinder.domain.dto.PathFinderResult;
import org.springframework.stereotype.Service;

@Service
public class PathFinderService {

    private final LineService lineService;

    private final PathFinder pathFinder;

    public PathFinderService(LineService lineService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathFinderResponse findOptimalPath(PathFinderRequest pathFinderRequest) {
        validatePathRequest(pathFinderRequest);

        PathFinderResult result = pathFinder.findOptimalPath(
                lineService.findAllLines(),
                lineService.findStation(pathFinderRequest.getDepartureStationId()),
                lineService.findStation(pathFinderRequest.getArrivalStationId()),
                PathFinderType.findType(pathFinderRequest.getPathFinderType()));

        return PathFinderConverter.convertToResponse(result);
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
