package nextstep.subway.application;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.path.PathFinderFactory;
import nextstep.subway.domain.path.PathType;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(final Long source, final Long target, final PathType type) {
        final Station sourceStation = stationService.findStationById(source);
        final Station targetStation = stationService.findStationById(target);
        final List<Line> lines = lineService.findAllLine();

        PathFinder pathFinder = PathFinderFactory.create(type);
        return pathFinder.findPath(lines, sourceStation, targetStation);
    }
}
