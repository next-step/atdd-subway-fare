package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Distance;
import nextstep.subway.domain.EdgeWeightStrategy;
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
    private Distance distance;

    public PathService(LineService lineService, StationService stationService, Distance distance) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.distance = distance;
    }

    public PathResponse findPath(Long source, Long target, EdgeWeightStrategy edgeWeightStrategy, int age) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);

        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, edgeWeightStrategy);

        if (edgeWeightStrategy instanceof Distance) {
            return PathResponse.of(path, age);
        }
        Path distancePath = subwayMap.findPath(upStation, downStation, distance);
        return PathResponse.of(path, distancePath, age);
    }

}
