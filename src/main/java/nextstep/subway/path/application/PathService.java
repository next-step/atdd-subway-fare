package nextstep.subway.path.application;

import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.domain.SubwayMap;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.FarePolicyRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
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

    public PathResponse findPath(Long source, Long target, PathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines, type);
        Path path = subwayMap.findPath(upStation, downStation);

        FarePolicyRequest farePolicyRequest = FarePolicyRequest.builder()
                .distance(path.extractDistance())
                .build();
        int fare = FareCalculator.calculate(farePolicyRequest);

        return PathResponse.of(path, fare);
    }
}
