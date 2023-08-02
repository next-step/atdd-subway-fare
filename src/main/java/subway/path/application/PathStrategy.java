package subway.path.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;

public interface PathStrategy {
    void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge);

    PathRetrieveResponse findPath(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections, List<Station> stationsInPath, Station sourceStation, Station targetStation);
}
