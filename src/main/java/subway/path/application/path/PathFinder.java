package subway.path.application.path;

import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;
import subway.path.application.dto.PathFinderRequest;
import subway.path.application.fare.PathFare;
import subway.path.application.graph.GraphBuilder;
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

    public Path findPath(PathFinderRequest request) {
        validIsSameOriginStation(request.getSourceStation(), request.getTargetStation());
        WeightedMultigraph<Station, SectionEdge> graph = this.graph.getGraph(request.getSections());
        List<Section> searchedSections = this.graph.getPath(graph, request.getSourceStation(), request.getTargetStation());

        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.from(request, searchedSections);
        long fare = pathFare.calculateFare(calcInfo);

        return Path.builder()
                .sections(searchedSections)
                .totalFare(fare).build();
    }

    private void validIsSameOriginStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_REQUEST_STATION_IS_SAME_ORIGIN);
        }
    }
}
