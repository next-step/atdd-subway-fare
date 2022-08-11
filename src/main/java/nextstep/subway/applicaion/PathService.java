package nextstep.subway.applicaion;

import nextstep.subway.builder.PathResponseBuilder;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.constant.SearchType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private PathResponseBuilder pathResponseBuilder;

    public PathService(LineService lineService, StationService stationService, PathResponseBuilder pathResponseBuilder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathResponseBuilder = pathResponseBuilder;
    }

    public PathResponse findPath(Long source, Long target, SearchType searchType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, searchType);

        return pathResponseBuilder.build(path);
    }
}
