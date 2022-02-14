package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SectionEdgeToWeightStrategy;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.SubwayMapGraphFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final SubwayMap subwayMap;

    public PathService(LineService lineService, StationService stationService, SubwayMap subwayMap) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.subwayMap = subwayMap;
    }

    public PathResponse findPath(Long source, Long target, SubwayMapGraphFactory subwayMapGraphFactory, FarePolicy farePolicy) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        Path path = subwayMap.findPath(subwayMapGraphFactory.createGraph(lines), upStation, downStation);
        return PathResponse.of(path, farePolicy.calculate(path));
    }
}
