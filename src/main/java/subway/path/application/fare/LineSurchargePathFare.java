package subway.path.application.fare;

import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LineSurchargePathFare extends PathFareChain {
    //    private final GraphBuilder graph;
//
//    public LineSurchargePathFare() {
//        this.graph = new GraphBuilder(new ShortestDistancePathFinder());
//    }
    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
//        WeightedMultigraph<Station, SectionEdge> sectionGraph = graph.getGraph(calcInfo.getSections());
//        List<Section> sectionsInPath = graph.getPath(sectionGraph, calcInfo.getSourceStation(), calcInfo.getTargetStation());
        List<Section> sections = calcInfo.getSections();
        List<Long> orderedSurcharges = sections.stream()
                .map(section -> section.getLine().getSurcharge())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        long maxSurcharge = orderedSurcharges.get(0) + calcInfo.getFare();
        PathFareCalculationInfo calcInfoResponse = calcInfo.withUpdatedFare(maxSurcharge);

        return super.nextCalculateFare(calcInfoResponse);
    }
}
