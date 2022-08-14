package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.PathResponse;
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

    public PathResponse findPath(Long source, Long target, int age) {
        Station upStation = findStation(source);
        Station downStation = findStation(target);
        SubwayMap subwayMap = getSubwayMap();
        Path path = subwayMap.findPath(upStation, downStation);

        return PathResponse.of(path, age);
    }

    public PathResponse findMinimumTimePath(final long source, final long target, int age) {
        Station upStation = findStation(source);
        Station downStation = findStation(target);
        SubwayMap subwayMap = getSubwayMap();
        Path path = subwayMap.findTimePath(upStation, downStation);

        return PathResponse.of(path, age);
    }

    private SubwayMap getSubwayMap() {
        List<Line> lines = lineService.findLines();
        return new SubwayMap(lines);
    }

    private Station findStation(final Long source) {
        return stationService.findById(source);
    }

}
