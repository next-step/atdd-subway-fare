package nextstep.subway.domain.sectiontype;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public interface SectionPathTypeCondition {

    void setEdgeWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section it, SectionEdge sectionEdge);

    boolean sameCondition(SectionPathType sectionPathType);

}
