package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.exception.PathException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.ui.exception.ExceptionMessage.SAME_STATION;

@Transactional
@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        if (source.equals(target)) {
            throw new PathException(SAME_STATION.getMsg());
        }
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        PathFinder pathFinder = new PathFinder(lines, type);
        Path path = pathFinder.shortsPath(sourceStation, targetStation);
        return new PathResponse(path.getStations(), path.pathTotalDistance(), path.pathTotalDuration(), path.fare(), 0);
    }
}
