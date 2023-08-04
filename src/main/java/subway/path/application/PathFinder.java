package subway.path.application;

import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.domain.Path;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;

public class PathFinder {

    private final GraphBuilder graph;
    private final PathFare pathFare = new PathFare();

    public PathFinder(PathFinderStrategy strategy) {
        this.graph = new GraphBuilder(strategy);
    }

    public Path findPath(List<Section> sections, Station sourceStation, Station targetStation) {
        validIsSameOriginStation(sourceStation, targetStation);
        WeightedMultigraph<Station, SectionEdge> graph = this.graph.getGraph(sections);
        List<Section> sectionsInPath = this.graph.getPath(graph, sourceStation, targetStation);

        long totalFareByDistance = pathFare.calculateFare(sections, sourceStation, targetStation);

        return Path.builder()
                .sections(sectionsInPath)
                .totalFare(totalFareByDistance).build();
    }


    private void validIsSameOriginStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_REQUEST_STATION_IS_SAME_ORIGIN);
        }
    }
}
