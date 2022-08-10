package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPathByDuration(Long source, Long target) {
        Station upStation = getStation(source);
        Station downStation = getStation(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPathByDuration(upStation, downStation);

        return getPathResponse(path);
    }

    public PathResponse findPathByDistance(Long source, Long target) {
        Station upStation = getStation(source);
        Station downStation = getStation(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPathByDistance(upStation, downStation);

        return getPathResponse(path);
    }

    public PathResponse getPathResponse(Path path) {
        List<StationResponse> stations = path.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        return new PathResponse(stations, distance, duration, Fare.of(distance).getFare());
    }

    private Station getStation(Long source) {
        return stationService.findById(source);
    }
}
