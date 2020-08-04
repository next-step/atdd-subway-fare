package nextstep.subway.maps.map.application;

import nextstep.subway.maps.line.application.LineService;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.domain.Money;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.line.dto.LineStationResponse;
import nextstep.subway.maps.map.domain.DiscountPolicyType;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.map.dto.MapResponse;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.map.dto.PathResponseAssembler;
import nextstep.subway.maps.station.application.StationService;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.dto.StationResponse;
import nextstep.subway.members.member.domain.LoginMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MapService {
    private LineService lineService;
    private StationService stationService;
    private PathService pathService;
    private FareCalculator fareCalculator;

    public MapService(LineService lineService, StationService stationService, PathService pathService, FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathService = pathService;
        this.fareCalculator = fareCalculator;
    }

    public MapResponse findMap() {
        List<Line> lines = lineService.findLines();
        Map<Long, Station> stations = findStations(lines);

        List<LineResponse> lineResponses = lines.stream()
                .map(it -> LineResponse.of(it, extractLineStationResponses(it, stations)))
                .collect(Collectors.toList());

        return new MapResponse(lineResponses);
    }

    private Map<Long, Station> findStations(List<Line> lines) {
        List<Long> stationIds = lines.stream()
                .flatMap(it -> it.getStationInOrder().stream())
                .map(LineStation::getStationId)
                .collect(Collectors.toList());

        return stationService.findStationsByIds(stationIds);
    }

    private List<LineStationResponse> extractLineStationResponses(Line line, Map<Long, Station> stations) {
        return line.getStationInOrder().stream()
                .map(it -> LineStationResponse.of(line.getId(), it, StationResponse.of(stations.get(it.getStationId()))))
                .collect(Collectors.toList());
    }

    public PathResponse findPath(Long source, Long target, PathType type, LocalDateTime time) {
        List<Line> lines = lineService.findLines();

        SubwayPath shortestPath = pathService.findPath(lines, source, target, PathType.DISTANCE, time);
        Money fare = fareCalculator.calculate(shortestPath);

        if (type == PathType.DISTANCE) {
            return assemblePathResponse(shortestPath, fare);
        }
        return assemblePathResponse(pathService.findPath(lines, source, target, type, time), fare);
    }

    public PathResponse findPath(LoginMember member, Long source, Long target, PathType type, LocalDateTime time) {
        List<Line> lines = lineService.findLines();

        SubwayPath shortestPath = pathService.findPath(lines, source, target, PathType.DISTANCE, time);
        Money fare = fareCalculator.calculate(shortestPath, DiscountPolicyType.ofAge(member.getAge()));

        if (type == PathType.DISTANCE) {
            return assemblePathResponse(shortestPath, fare);
        }
        return assemblePathResponse(pathService.findPath(lines, source, target, type, time), fare);
    }

    private PathResponse assemblePathResponse(SubwayPath subwayPath, Money fare) {
        Map<Long, Station> stations = stationService.findStationsByIds(subwayPath.extractStationId());
        return PathResponseAssembler.assemble(subwayPath, stations, fare);
    }
}
