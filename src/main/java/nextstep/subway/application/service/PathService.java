package nextstep.subway.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.response.PathResponse;
import nextstep.subway.application.exception.CanNotFindPathException;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Path;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.entity.SubWayFare;
import nextstep.subway.domain.entity.SubwayMap;
import nextstep.subway.domain.enums.PathSearchType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathResponse findPath(Long sourceStationId, Long targetStationId, PathSearchType type) {
        Station source = stationService.getStationById(sourceStationId);
        Station target = stationService.getStationById(targetStationId);
        List<Line> lines = lineService.getLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path shortestPath = subwayMap.findShortestPath(source, target, type)
            .orElseThrow(() -> new CanNotFindPathException("Unable to find the shortest path."));
        SubWayFare subWayFare = new SubWayFare(shortestPath);
        return new PathResponse(
            shortestPath.getStationDtoOfPath(target),
            shortestPath.getDistance(),
            shortestPath.getDuration(),
            subWayFare.calculateFare()
        );
    }


}
