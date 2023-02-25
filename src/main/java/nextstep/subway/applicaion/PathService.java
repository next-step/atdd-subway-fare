package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private FarePolicy farePolicy;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.farePolicy = new BaseFarePolicy();
    }

    public PathResponse findPath(PathRequest pathRequest) {
        return findPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType());
    }

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        SubwayMap subwayMap = createSubwayMap(pathType);
        Path path = subwayMap.findPath(upStation, downStation);
        return PathResponse.of(path);
    }

    private SubwayMap createSubwayMap(PathType pathType) {
        List<Line> lines = lineService.findLines();
        return new SubwayMap(lines, pathType, farePolicy);
    }
}
