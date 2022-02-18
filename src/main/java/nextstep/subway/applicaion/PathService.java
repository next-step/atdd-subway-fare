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
            throw new PathException("출발역과 도착역을 다르게 설정해주세요.");
        }
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        PathFinder pathFinder = new PathFinder(lines);
        List<Station> stations = pathFinder.shortsPathStations(sourceStation, targetStation, type);
        Path path = pathFinder.shortsPath(sourceStation, targetStation, type);

        return new PathResponse(stations, path.getDistance(), path.getDuration());
    }
}
