package subway.path.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.domain.SectionEdge;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;

@Component
public class MinimumTimePathFinder extends AbstractPathFinder implements PathStrategy {

    @Override
    public PathRetrieveResponse findPath(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections, List<Station> stationsInPath, Station sourceStation, Station targetStation) {
        Long totalDistance = getDistanceInMinimumTimePath(stationsInPath, sections);
        Double minimumWeight = getWeightOfPath(graph, sourceStation, targetStation);

        return PathRetrieveResponse.builder()
                .stations(StationResponse.from(stationsInPath))
                .distance(totalDistance)
                .duration(minimumWeight.longValue())
                .build();
    }

    @Override
    public void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge) {
        graph.setEdgeWeight(edge, section.getDuration());
    }

    private Long getDistanceInMinimumTimePath(List<Station> stationsInPath, List<Section> sections) {
        List<Section> sectionsInPath = getSections(stationsInPath, sections);
        return sectionsInPath.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }
}
