package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.path.SubwayMap;
import nextstep.subway.price.DistancePricePolicy;
import nextstep.subway.price.PricePolicy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType.getEdgeInitiator());
        int shortestDistance = subwayMap.findPath(upStation, downStation, PathType.DISTANCE.getEdgeInitiator()).extractDistance();

        PricePolicy price = new DistancePricePolicy(shortestDistance);
        return PathResponse.of(path, price.calculatePrice());
    }
}
