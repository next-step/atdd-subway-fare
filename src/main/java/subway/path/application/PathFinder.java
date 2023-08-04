package subway.path.application;

import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;
import subway.path.application.dto.PathFinderRequest;
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

//        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder() // TODO : 이거 집어넣으세요...
//                .sourceStation(request.getSourceStation())
//                .targetStation(request.getTargetStation())
//                .wholeSections(request.getSections())
//                .searchedSections(searchedSections)
//                .member(request.getMember())
//                .build();
//        long totalFareByDistance = pathFare.calculateFare(calcInfo);
        long totalFareByDistance = pathFare.calculateFare(request.getSections(), request.getSourceStation(), request.getTargetStation());

        return Path.builder()
                .sections(searchedSections)
                .totalFare(totalFareByDistance).build();
    }

    private void validIsSameOriginStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_REQUEST_STATION_IS_SAME_ORIGIN);
        }
    }
}
