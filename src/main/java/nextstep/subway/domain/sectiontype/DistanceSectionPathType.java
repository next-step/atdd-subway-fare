package nextstep.subway.domain.sectiontype;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class DistanceSectionPathType implements SectionPathTypeCondition{

    private SectionPathType type = SectionPathType.DISTANCE;

    @Override
    public boolean sameCondition(SectionPathType sectionPathType) {
        return type.equals(sectionPathType);
    }

    public void setEdgeWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section it, SectionEdge sectionEdge) {
          graph.setEdgeWeight(sectionEdge, it.getDistance());
    }
}
