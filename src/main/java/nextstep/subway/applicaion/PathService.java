package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.path.SubwayMap;
import nextstep.subway.price.PricePolicyCalculate;
import nextstep.subway.price.PricePolicy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathResponse findPath(Long source, Long target, PathType pathType, Integer age) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathType.getEdgeInitiator());

        int shortestDistance = this.getShortDistance(pathType, path, subwayMap, upStation, downStation);
        PricePolicy pricePolicy = new PricePolicyCalculate(shortestDistance, age, path.getAddtionPrice());
        return PathResponse.of(path, pricePolicy.calculatePrice());
    }

    private int getShortDistance(PathType pathType, Path path, SubwayMap subwayMap, Station upStation, Station downStation) {
        if(PathType.DISTANCE.equals(pathType)) {
            return path.extractDistance();
        }
        return subwayMap.findPath(upStation, downStation, PathType.DISTANCE.getEdgeInitiator()).extractDistance();
    }

}
