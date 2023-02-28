package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SearchType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
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

    public PathResponse findPath(Long source, Long target, SearchType searchType) {
        Station departureStation = stationService.findById(source);
        Station destinationStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        SubwayMap subwayMap = new SubwayMap(lines, searchType);
        Path path = subwayMap.findPath(departureStation, destinationStation);

        return PathResponse.of(path);
    }
}
