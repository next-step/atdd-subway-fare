package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
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

    public PathResponse findPath(Long source, Long target) {
        return getPathResponse(source, target, SectionPathType.DISTANCE);
    }

    public PathResponse findPathDuration(Long source, Long target) {
        return getPathResponse(source, target, SectionPathType.DURATION);
    }

    private PathResponse getPathResponse(Long source, Long target, SectionPathType sectionPathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, sectionPathType);
        Path path = subwayMap.findPath(upStation, downStation);
        return PathResponse.of(path);
    }
}
