package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FindPathType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, String typeName) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        FindPathType findPathType = FindPathType.find(typeName);
        Path path = subwayMap.findPath(upStation, downStation, findPathType);
        return PathResponse.of(path);
    }
}
