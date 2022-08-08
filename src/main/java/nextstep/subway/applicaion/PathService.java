package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FareType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathCondition;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.FareStrategy;
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

    public PathResponse findShortestPath(Long source, Long target, PathCondition pathCondition) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation, pathCondition.getEdgeInitiator());
        int shortestDistance = subwayMap.findShortestPathDistance(upStation, downStation);
        int fare = calculateFare(shortestDistance);

        return PathResponse.of(path, fare);
    }

    private int calculateFare(int distance) {
        FareStrategy fareStrategy = FareType.findStrategy(distance);
        return fareStrategy.calculate(distance);
    }
}
