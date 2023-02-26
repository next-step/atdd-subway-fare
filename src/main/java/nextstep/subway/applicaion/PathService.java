package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.policy.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathRequestType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, type);
        path.apply(formulateFarePolicies());

        return PathResponse.of(path);
    }

    private FarePolicies formulateFarePolicies(){
        FarePolicy base = new FixedFarePolicy(1250);
        FarePolicy ten = new BetweenUnitFarePolicy(10, 50, 5, 100);
        FarePolicy fifth = new GreaterUnitFarePolicy(50, 8, 100);

        FarePolicies farePolicies = new FarePolicies();
        farePolicies.addPolicies(base, ten, fifth);

        return farePolicies;
    }
}
