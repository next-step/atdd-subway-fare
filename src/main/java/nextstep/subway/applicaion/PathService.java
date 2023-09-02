package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FindPathType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.FareChain;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private FareChain fareChain;

    public PathService(LineService lineService, StationService stationService, FareChain fareChain) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareChain = fareChain;
    }

    public PathResponse findPath(Long source, Long target, FindPathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, type);

        return PathResponse.of(path, fareChain, null);
    }
}
