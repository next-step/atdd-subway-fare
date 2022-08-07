package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.subwaymap.DistanceSubwayMap;
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
        DistanceSubwayMap subwayMap = new DistanceSubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path);
    }
}
