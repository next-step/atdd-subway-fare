package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.PathService;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FareService {
    private final PathService pathService;
    private final FareCalculator fareCalculator;

    public FareService(PathService pathService, FareCalculator fareCalculator) {
        this.pathService = pathService;
        this.fareCalculator = fareCalculator;
    }

    public Fare calculateFare(List<Line> lines, SubwayPath subwayPath, MemberResponse member, PathType type) {

        if (type != PathType.DISTANCE) {
            return calculateFare(lines, subwayPath, member);
        }

        FareContext fareContext = new FareContext(subwayPath, member);
        return fareCalculator.calculate(fareContext);
    }

    private Fare calculateFare(List<Line> lines, SubwayPath subwayPath, MemberResponse member) {
        Long source = subwayPath.getSourceStationId();
        Long target = subwayPath.getTargetStationId();

        SubwayPath pathForCalculate = pathService.findPath(lines, source, target, PathType.DISTANCE);
        FareContext fareContext = new FareContext(pathForCalculate, member);
        return fareCalculator.calculate(fareContext);
    }
}