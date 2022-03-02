package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.WeightType;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private SubwayMap subwayMap;

    public PathService(LineService lineService, StationService stationService, SubwayMap subwayMap) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.subwayMap = subwayMap;
    }

    public PathResponse findPath(int age, Long source, Long target, WeightType weightType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        subwayMap.setUp(lines);
        Path path = subwayMap.findPath(upStation, downStation, weightType);

        return PathResponse.of(path, age);
    }
}
