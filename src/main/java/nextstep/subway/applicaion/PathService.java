package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SearchType;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final ShortestPathFinder shortestPathFinder;

    public PathService(final LineService lineService, final StationService stationService, final ShortestPathFinder shortestPathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.shortestPathFinder = shortestPathFinder;
    }

    public PathResponse findPath(final Long source, final Long target, final String type) {
        final Station upStation = stationService.findById(source);
        final Station downStation = stationService.findById(target);
        final List<Line> lines = lineService.findLines();
        final Path path = shortestPathFinder.findPath(lines, upStation, downStation, SearchType.from(type));

        return PathResponse.of(path);
    }
}
