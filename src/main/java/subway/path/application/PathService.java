package subway.path.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.application.LineService;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.domain.PathRetrieveType;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final ShortestDistancePathFinder shortestDistancePathFinder;
    private final MinimumTimePathFinder minimumTimePathFinder;

    public PathRetrieveResponse getPath(long sourceStationId, long targetStationId, PathRetrieveType type) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Line> lines = lineService.findByStation(sourceStation, targetStation);
        List<Section> sections = getAllSections(lines);

        PathRetrieveResponse pathFind = PathRetrieveResponse.builder().build();
        if (type.equals(PathRetrieveType.DISTANCE)) {
            pathFind = shortestDistancePathFinder.findPath(sections, sourceStation, targetStation);
        }
        if (type.equals(PathRetrieveType.DURATION)) {
            pathFind = minimumTimePathFinder.findPath(sections, sourceStation, targetStation);
        }
        return pathFind;
    }

    public void checkPathValidation(Station sourceStation, Station targetStation) {
        List<Line> lines = lineService.findByStation(sourceStation, targetStation);
        List<Section> sections = getAllSections(lines);
        shortestDistancePathFinder.findPath(sections, sourceStation, targetStation);
    }

    private List<Section> getAllSections(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getLineSections().getSections().stream())
                .collect(Collectors.toList());
    }


}
