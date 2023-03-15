package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.response.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.path.PathFinderFactory;
import nextstep.subway.domain.path.PathType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(final Long sourceId, final Long targetId, final String type) {
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        List<Line> lines = lineService.findLines();

        PathFinder pathFinder = PathFinderFactory.drawMap(lines, PathType.valueOf(type));
        Path path = pathFinder.findPath(sourceStation, targetStation);

        return PathResponse.of(path);
    }
}
