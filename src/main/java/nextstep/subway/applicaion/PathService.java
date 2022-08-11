package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.finder.PathFinder;
import nextstep.subway.domain.path.PathType;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        PathFinder pathFinder = pathType.getPathFinder(lines);
        Path path = pathFinder.findPath(upStation, downStation);

        return PathResponse.of(path);
    }
}
