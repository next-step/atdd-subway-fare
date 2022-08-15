package nextstep.path.applicaion;

import nextstep.line.application.LineService;
import nextstep.line.domain.Line;
import nextstep.path.applicaion.dto.PathResponse;
import nextstep.path.domain.FareCalculator;
import nextstep.path.domain.Path;
import nextstep.path.domain.PathSearchType;
import nextstep.path.domain.SubwayMap;
import nextstep.station.application.StationService;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathSearchType searchType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation.getId(), downStation.getId(), searchType);
        List<StationResponse> stations = createStationResponses(path.getStations());

        int fare = new FareCalculator().calculateFare(subwayMap.shortestDistance(source, target));
        return new PathResponse(stations, path, fare);
    }

    private List<StationResponse> createStationResponses(List<Long> stationIds) {
        return stationService.findAllStationsById(stationIds)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
