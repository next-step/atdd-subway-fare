package nextstep.path.application;

import nextstep.line.application.LineProvider;
import nextstep.line.domain.Line;
import nextstep.path.application.dto.PathResponse;
import nextstep.path.application.dto.PathSearchRequest;
import nextstep.path.application.fare.FareCalculator;
import nextstep.path.domain.Path;
import nextstep.path.domain.PathType;
import nextstep.path.domain.SubwayMap;
import nextstep.path.exception.PathNotFoundException;
import nextstep.path.exception.PathSearchNotValidException;
import nextstep.station.domain.Station;
import nextstep.station.exception.StationNotExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineProvider lineProvider;
    private final FareCalculator fareCalculator;

    public PathService(final LineProvider lineProvider) {
        this.lineProvider = lineProvider;
        this.fareCalculator = new FareCalculator();
    }

    public PathResponse findShortestPath(final PathSearchRequest searchRequest) {
        if (Objects.equals(searchRequest.getSource(), searchRequest.getTarget())) {
            throw new PathSearchNotValidException("target can not be the same with source");
        }

        final Path targetShortestPath = getShortestPath(searchRequest.getSource(), searchRequest.getTarget(), searchRequest.getPathType())
                .orElseThrow(PathNotFoundException::new);

        return PathResponse.from(targetShortestPath, calculateFare(searchRequest));
    }

    private long calculateFare(final PathSearchRequest searchRequest) {
        final Path distanceShortestPath = getShortestPath(searchRequest.getSource(), searchRequest.getTarget(), PathType.DISTANCE)
                .orElseThrow(PathNotFoundException::new);
        return fareCalculator.calculate(distanceShortestPath, searchRequest.getAge());
    }

    public boolean isInvalidPath(final PathSearchRequest searchRequest) {
        return getShortestPath(searchRequest.getSource(), searchRequest.getTarget(), searchRequest.getPathType()).isEmpty();
    }

    private Optional<Path> getShortestPath(final Long source, final Long target, final PathType pathType) {
        final List<Line> allLines = lineProvider.getAllLines();
        final Map<Long, Station> stationMap = createStationMapFrom(allLines);
        final Station sourceStation = stationMap.computeIfAbsent(source, throwStationNotFoundException());
        final Station targetStation = stationMap.computeIfAbsent(target, throwStationNotFoundException());

        final SubwayMap subwayMap = new SubwayMap(allLines);
        return subwayMap.findShortestPath(sourceStation, targetStation, pathType);
    }

    private Map<Long, Station> createStationMapFrom(final List<Line> allLines) {
        return allLines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private static Function<Long, Station> throwStationNotFoundException() {
        return id -> {
            throw new StationNotExistException(id);
        };
    }
}
