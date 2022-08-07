package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.subwaymap.DistanceSubwayMap;
import nextstep.subway.domain.subwaymap.DurationSubwayMap;
import nextstep.subway.domain.subwaymap.SubwayMap;
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

        SubwayMap subwayMap = type == PathType.DISTANCE
                ? new DistanceSubwayMap(lines)
                : new DurationSubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation);
        return PathResponse.of(path);
    }
}
