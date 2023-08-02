package nextstep.subway.domain;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;

public class SubwayByDistanceMap extends SubwayMap {


    public SubwayByDistanceMap(List<Line> lines) {
        super(lines);
    }

    @Override
    protected void setEdgeWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, SectionEdge sectionEdge, Section section) {
        graph.setEdgeWeight(sectionEdge, section.getDistance());
    }

}
