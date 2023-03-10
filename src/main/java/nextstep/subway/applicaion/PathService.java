package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final FareService fareService;

    public PathService(LineService lineService, StationService stationService, FareService fareService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareService = fareService;
    }

    public PathResponse findPath(int age, Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, pathType);
        Path path = subwayMap.findPath(upStation, downStation);

        int totalFare = fareService.totalFare(path.extractDistance(), path.extraLineFare(), age);
        return PathResponse.of(path, totalFare);
    }
}
