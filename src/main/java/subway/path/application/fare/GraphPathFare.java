package subway.path.application.fare;

import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;
import subway.path.application.graph.GraphBuilder;
import subway.path.application.path.ShortestDistancePathFinder;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;

public class GraphPathFare extends PathFareChain {
    private final GraphBuilder graph;
    public GraphPathFare() {
        this.graph = new GraphBuilder(new ShortestDistancePathFinder());
    }
    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
        WeightedMultigraph<Station, SectionEdge> fareSectionGraph = graph.getGraph(calcInfo.getSections());
        List<Section> fareSections = graph.getPath(fareSectionGraph, calcInfo.getSourceStation(), calcInfo.getTargetStation());

        PathFareCalculationInfo calcInfoResponse = calcInfo.withUpdatedSections(fareSections);
        return super.nextCalculateFare(calcInfoResponse);
    }
}
