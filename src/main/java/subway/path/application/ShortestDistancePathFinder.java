package subway.path.application;


import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;

@Component
public class ShortestDistancePathFinder extends AbstractPathFinder implements PathStrategy {

    @Override
    public PathRetrieveResponse findPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections, List<Station> stationsInPath,Station sourceStation, Station targetStation) {
        Long totalDuration = getDurationInShortestDistancePath(stationsInPath, sections);
        Double minimumWeight = getWeightOfPath(graph, sourceStation, targetStation);

        return PathRetrieveResponse.builder()
                .stations(StationResponse.from(stationsInPath))
                .distance(minimumWeight.longValue())
                .duration(totalDuration)
                .build();
    }

    @Override
    public void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section, DefaultWeightedEdge edge) {
        graph.setEdgeWeight(edge, section.getDistance());
    }

    private Long getDurationInShortestDistancePath(List<Station> stationsInPath, List<Section> sections) {
        List<Section> sectionsInPath = getSections(stationsInPath, sections);
        return sectionsInPath.stream()
                .map(Section::getDuration)
                .reduce(0L, Long::sum);
    }

}
