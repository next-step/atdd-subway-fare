package nextstep.subway.domain;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public enum SectionPathType {
    DISTANCE, DURATION;

    public void setEdgeWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section it, SectionEdge sectionEdge) {
        switch (this) {
            case DISTANCE:
                graph.setEdgeWeight(sectionEdge, it.getDistance());
                break;
            case DURATION:
                graph.setEdgeWeight(sectionEdge, it.getDuration());
                break;
        }
    }
}
