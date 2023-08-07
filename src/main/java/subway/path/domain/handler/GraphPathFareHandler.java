package subway.path.domain.handler;

import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;
import subway.path.domain.graph.GraphBuilder;
import subway.path.domain.strategy.ShortestDistancePathFinderStrategy;
import subway.path.domain.graph.SectionEdge;
import subway.station.domain.Station;

import java.util.List;

public class GraphPathFareHandler extends PathFareChain {
    private final GraphBuilder graph;
    public GraphPathFareHandler() {
        this.graph = new GraphBuilder(new ShortestDistancePathFinderStrategy());
    }
    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
        WeightedMultigraph<Station, SectionEdge> fareSectionGraph = graph.getGraph(calcInfo.getSections());
        List<Section> fareSections = graph.getPath(fareSectionGraph, calcInfo.getSourceStation(), calcInfo.getTargetStation());

        PathFareCalculationInfo calcInfoResponse = calcInfo.withUpdatedSections(fareSections);
        return super.nextCalculateFare(calcInfoResponse);
    }
}
