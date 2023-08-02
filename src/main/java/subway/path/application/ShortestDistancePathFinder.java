package subway.path.application;


import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.domain.SectionEdge;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;

@Component
public class ShortestDistancePathFinder extends AbstractPathFinder implements PathStrategy {

    @Override
    public PathRetrieveResponse findPath(List<Section> sections, Station sourceStation, Station targetStation) {
        Long totalDistance = getTotalDistanceInPath(sections);
        Long totalDuration = getTotalDurationInPath(sections);
        long totalFareFromDistance = calculateFare(totalDistance);

        return PathRetrieveResponse.builder()
                .stations(StationResponse.from(sections))
                .distance(totalDistance)
                .duration(totalDuration)
                .fare(totalFareFromDistance)
                .build();
    }

    @Override
    public void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, Section section, SectionEdge edge) {
        graph.setEdgeWeight(edge, section.getDistance());
    }
}
