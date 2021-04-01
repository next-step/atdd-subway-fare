package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceId, Long targetId, PathType type) {
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        SubwayGraph subwayGraph = findGraph(type, source, target);
        PathResult pathResult = subwayGraph.findPath();
        int totalDistance = pathResult.getTotalDistance();
        int extraCharge = pathResult.getLineMaxExtraCharge();

        FareRuleStrategy fareRuleStrategy = FareStrategyFactory.from(totalDistance);
        Fare fare = new Fare(fareRuleStrategy, totalDistance);
        fare.addExtraCharge(extraCharge);

        return PathResponse.of(pathResult, fare);
    }

    private SubwayGraph findGraph(PathType type, Station sourceStation, Station targetStation) {
        List<Line> lines = lineService.findAllLines();
        List<Section> sections = getSections(lines);
        return new SubwayGraph(sections, type, sourceStation, targetStation);
    }

    private List<Section> getSections(List<Line> lines) {
        return lines.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
