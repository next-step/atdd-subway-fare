package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.util.SubwayMap;
import nextstep.subway.util.SubwayMapFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final SubwayMapFactory subwayMapFactory;

    public PathService(LineService lineService, StationService stationService, SubwayMapFactory subwayMapFactory) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.subwayMapFactory = subwayMapFactory;
    }

    public PathResponse findPath(Long source, Long target, String type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = subwayMapFactory.subwayMap(type);
        Path path = subwayMap.findPath(lines, upStation, downStation);

        return PathResponse.of(path);
    }
}
