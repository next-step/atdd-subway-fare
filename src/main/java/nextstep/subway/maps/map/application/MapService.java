package nextstep.subway.maps.map.application;

import nextstep.subway.maps.boarding.service.BoardingService;
import nextstep.subway.maps.line.application.LineService;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.line.dto.LineStationResponse;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.map.dto.MapResponse;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.map.dto.PathResponseAssembler;
import nextstep.subway.maps.station.application.StationService;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathService pathService;
    private final BoardingService boardingService;

    public MapService(LineService lineService, StationService stationService, PathService pathService, BoardingService boardingService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathService = pathService;
        this.boardingService = boardingService;
    }

    public MapResponse findMap() {
        List<Line> lines = lineService.findLines();
        Map<Long, Station> stations = findStations(lines);

        List<LineResponse> lineResponses = lines.stream()
                .map(it -> LineResponse.of(it, extractLineStationResponses(it, stations)))
                .collect(Collectors.toList());

        return new MapResponse(lineResponses);
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        return findPath(source, target, type, null);
    }

    public PathResponse findPath(Long source, Long target, PathType type, Long memberId) {
        List<Line> lines = lineService.findLines();
        SubwayPath subwayPath = pathService.findPath(lines, source, target, type);
        Map<Long, Station> stations = stationService.findStationsByIds(subwayPath.extractStationId());
        final int fare = boardingService.calculateFare(subwayPath, memberId);

        return PathResponseAssembler.assemble(subwayPath, stations, fare);
    }

    private Map<Long, Station> findStations(List<Line> lines) {
        List<Long> stationIds = lines.stream()
                .flatMap(it -> it.getStationInOrder().stream())
                .map(it -> it.getStationId())
                .collect(Collectors.toList());

        return stationService.findStationsByIds(stationIds);
    }

    private List<LineStationResponse> extractLineStationResponses(Line line, Map<Long, Station> stations) {
        return line.getStationInOrder().stream()
                .map(it -> LineStationResponse.of(line.getId(), it, StationResponse.of(stations.get(it.getStationId()))))
                .collect(Collectors.toList());
    }
}
