package nextstep.subway.applicaion.edge;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public interface EdgeInitiator {

    void initEdges(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph);

    void initOppositeEdges(List<Line> lines,
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph);
}
