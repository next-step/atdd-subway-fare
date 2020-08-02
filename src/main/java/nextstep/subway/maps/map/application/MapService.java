package nextstep.subway.maps.map.application;

import nextstep.subway.maps.fare.application.FareService;
import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.line.application.LineService;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
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
import nextstep.subway.members.member.domain.LoginMember;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapService {

    private LineService lineService;
    private StationService stationService;
    private PathService pathService;
    private FareService fareService;

    public MapService(LineService lineService, StationService stationService, PathService pathService, FareService fareService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathService = pathService;
        this.fareService = fareService;
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
        List<Line> lines = lineService.findLines();
        SubwayPath subwayPath = pathService.findPath(lines, source, target, type);
        Map<Long, Station> stations = stationService.findStationsByIds(subwayPath.extractStationId());
        Fare fare = fareService.calculateFare(lines, subwayPath, null, type);
        return PathResponseAssembler.assemble(subwayPath, stations, fare.getValue());
    }

    public PathResponse findPath(LoginMember member, Long source, Long target, PathType type) {
        return null;
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
}
