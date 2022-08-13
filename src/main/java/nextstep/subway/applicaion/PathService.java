package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, String type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        return PathResponse.of(
                getPathByType(upStation, downStation, lines, type),
                getPathByShortestDistanceFare(upStation, downStation, lines)
        );
    }

    private Path getPathByType(Station upStation, Station downStation, List<Line> lines, String type) {
        SubwayMap subwayMap = new SubwayMap(lines, PathType.valueOf(type));
        return subwayMap.findPath(upStation, downStation);
    }


    private int getPathByShortestDistanceFare(Station upStation, Station downStation, List<Line> lines){
        SubwayMap subwayMap = new SubwayMap(lines, PathType.DISTANCE);
        Path path = subwayMap.findPath(upStation, downStation);
        int distance = path.extractDistance();
        return FarePolicy.createFarePolicy(distance).getFare(distance);
    }
}
