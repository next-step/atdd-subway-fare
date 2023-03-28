package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.PathFinderFactory;
import nextstep.subway.domain.path.PathSearch;

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

    public PathResponse findPath(Long source, Long target, PathSearch searchType) {

        List<Line> lines = lineService.findLines();
        PathFinder pathFinder = PathFinderFactory.getPathFinder(lines, searchType);

        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);

        Path path = pathFinder.findPath(upStation, downStation);

        return PathResponse.of(path);
    }
}
