package nextstep.path.application;

import nextstep.line.domain.SectionRepository;
import nextstep.path.application.dto.PathsResponse;
import nextstep.fare.domain.Fare;
import nextstep.path.domain.PathType;
import nextstep.path.domain.ShortestPath;
import nextstep.path.ui.exception.SameSourceAndTargetException;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.path.domain.PathType.DISTANCE;

@Service
@Transactional(readOnly = true)
public class PathService {

    private SectionRepository sectionRepository;

    public PathService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public PathsResponse findShortestPaths(Long source, Long target, String type) {
        ShortestPath shortestPath = createShortestPath(source, target, PathType.lookUp(type));

        Station start = shortestPath.lookUpStation(source);
        Station end = shortestPath.lookUpStation(target);

        return createPathsResponse(shortestPath, start, end);
    }

    private PathsResponse createPathsResponse(ShortestPath shortestPath, Station start, Station end) {
        int distance = shortestPath.getDistance(start, end);

        return PathsResponse.builder()
                .distance(distance)
                .duration(shortestPath.getDuration(start, end))
                .fare(Fare.from(distance))
                .stations(createStationResponses(shortestPath.getStations(start, end)))
                .build();
    }

    public void validatePaths(Long source, Long target) {
        ShortestPath shortestPath = createShortestPath(source, target, DISTANCE);

        Station start = shortestPath.lookUpStation(source);
        Station end = shortestPath.lookUpStation(target);

        shortestPath.validateConnected(start, end);
    }

    private ShortestPath createShortestPath(Long source, Long target, PathType pathType) {
        validateSameSourceAndTarget(source, target);
        return pathType.createShortestPath(sectionRepository.findAll());
    }

    private static void validateSameSourceAndTarget(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameSourceAndTargetException();
        }
    }

    private List<StationResponse> createStationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
