package nextstep.subway.maps.map.dto;

import nextstep.subway.maps.line.application.LineService;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.PathService;
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

        // 최단경로 요금 계산
        List<Line> lines = lineService.findLines();

        Long source = subwayPath.getLineStationEdges().get(0).getLineStation().getStationId();
        Long target = subwayPath.getLineStationEdges().get(subwayPath.getLineStationEdges().size()-1).getLineStation().getStationId();
        SubwayPath shortestDistancePath = pathService.findShortestDistancePath(lines, source, target);

        int shortestDistance = shortestDistancePath.calculateDistance();
        int distance = subwayPath.calculateDistance();
        int fare;

        if (shortestDistance < distance) {
            fare = shortestDistancePath.calculateFare(shortestDistance);
            return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
        }

        fare = subwayPath.calculateFare(distance);

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
    }
}
