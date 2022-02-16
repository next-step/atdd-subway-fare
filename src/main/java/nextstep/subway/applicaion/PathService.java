package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.discountcondition.FareDiscountCondition;
import nextstep.subway.domain.map.SubwayMap;
import nextstep.subway.domain.map.SubwayMapGraphFactory;

@Service
public class PathService {
    private final FareCalculator fareCalculator;
    private final LineService lineService;
    private final StationService stationService;
    private final SubwayMap subwayMap;

    public PathService(FareCalculator fareCalculator, LineService lineService, StationService stationService,
                       SubwayMap subwayMap) {
        this.fareCalculator = fareCalculator;
        this.lineService = lineService;
        this.stationService = stationService;
        this.subwayMap = subwayMap;
    }

    public PathResponse findPath(Long source, Long target,
                                 SubwayMapGraphFactory subwayMapGraphFactory,
                                 FareDiscountCondition fareDiscountPolicy) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        Path path = subwayMap.findPath(subwayMapGraphFactory.createGraph(lines), upStation, downStation);
        int totalCost = fareDiscountPolicy.discount(
            fareCalculator.calculate(path)
        );
        return PathResponse.of(path, totalCost);
    }
}
