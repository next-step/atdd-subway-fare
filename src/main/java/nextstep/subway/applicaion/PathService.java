package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.path.PathStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    private Map<String, PathStrategy> pathStrategyMap;

    public PathService(LineService lineService, StationService stationService, Map<String, PathStrategy> pathStrategyMap) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathStrategyMap = pathStrategyMap;
    }

    public PathResponse findPath(Long source, Long target, String type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines, pathStrategyMap.get(type));
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path);
    }
}
