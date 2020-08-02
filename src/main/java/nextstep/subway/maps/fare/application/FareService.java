package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.PathService;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.domain.Member;
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

    public Fare calculateFare(List<Line> lines, SubwayPath subwayPath, Member member, PathType type) {
        List<LineStationEdge> lineStationEdges = subwayPath.getLineStationEdges();

        if (type != PathType.DISTANCE) {
            return calculateFare(lines, lineStationEdges, member);
        }

        FareContext fareContext = new FareContext(subwayPath, member);
        return fareCalculator.calculate(fareContext);
    }

    private Fare calculateFare(List<Line> lines, List<LineStationEdge> lineStationEdges, Member member) {
        Long source = lineStationEdges.stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("lineStationEdges is empty"))
                .getLineStation().getStationId();

        int size = lineStationEdges.size();
        Long target = lineStationEdges.stream().skip(size - 1).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("lineStationEdges is empty"))
                .getLineStation().getStationId();

        SubwayPath pathForCalculate = pathService.findPath(lines, source, target, PathType.DISTANCE);
        FareContext fareContext = new FareContext(pathForCalculate, member);
        return fareCalculator.calculate(fareContext);
    }
}