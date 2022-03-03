package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.pathtype.PathTypeProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private PathTypeProvider pathTypeProvider;

    public PathService(LineService lineService, StationService stationService, PathTypeProvider pathTypeProvider) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathTypeProvider = pathTypeProvider;
    }

    public PathResponse findPath(Long source, Long target, String type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, pathTypeProvider.provide(type));
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path);
    }
}
