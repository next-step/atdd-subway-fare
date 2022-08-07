package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.pathfinder.PathFinder;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final List<PathFinder> pathFinders;

    public PathResponse findPath(Long source, Long target, PathType type) {
        final PathFinder pathFinder = findPathFinder(type);

        Path path = pathFinder.findPath(
                lineService.findLines(),
                stationService.findById(source),
                stationService.findById(target));

        return PathResponse.of(path);
    }

    private PathFinder findPathFinder(final PathType type) {
        return pathFinders.stream()
                .filter(v -> v.findPathFinder(type) != null)
                .findAny()
                .orElseThrow();
    }
}
