package nextstep.subway.maps.map.dto;

import nextstep.subway.maps.line.application.LineService;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.PathService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.dto.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    LineService lineService;
    PathService pathService;

    public PathResponseAssembler(LineService lineService, PathService pathService) {
        this.lineService = lineService;
        this.pathService = pathService;
    }

    public PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations) {
        List<StationResponse> stationResponses = subwayPath.extractStationId().stream()
                .map(it -> StationResponse.of(stations.get(it)))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = subwayPath.calculateFare(distance);

        if (subwayPath.getLineStationEdges().size() != 0) {
            // 거리 비례제 계산을 위한 최단경로 요금 계산
            List<Line> lines = lineService.findLines();

            int sourceIndex = 0;
            int targetIndex = subwayPath.getLineStationEdges().size() - 1;

            Long source = subwayPath.getLineStationEdges().get(sourceIndex).getLineStation().getStationId();
            Long target = subwayPath.getLineStationEdges().get(targetIndex).getLineStation().getStationId();
            SubwayPath shortestDistancePath = pathService.findPath(lines, source, target, PathType.DISTANCE);

            int shortestDistance = shortestDistancePath.calculateDistance();

            if (shortestDistance < distance) {
                fare = shortestDistancePath.calculateFare(shortestDistance);
                return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
            }
        }

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
    }
}
