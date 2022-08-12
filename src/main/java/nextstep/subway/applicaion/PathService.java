package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathCondition;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import support.auth.userdetails.User;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(User user, Long source, Long target, PathCondition pathCondition) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation, pathCondition.getEdgeInitiator());
        path.setAge(user.getAge());
        if (pathCondition == PathCondition.DURATION) {
            int shortestDistance = subwayMap.findShortestPathDistance(upStation, downStation);
            path.setShortestDistance(shortestDistance);
        }

        return PathResponse.of(path);
    }
}
