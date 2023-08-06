package subway.path.domain;

import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;
import subway.path.application.dto.PathFinderRequest;
import subway.path.domain.graph.GraphBuilder;
import subway.path.domain.graph.SectionEdge;
import subway.path.domain.strategy.PathFinderStrategy;
import subway.station.domain.Station;

import java.util.List;

public class PathFinder {

    private final GraphBuilder strategyGraph;
    private final PathFare pathFare = new PathFare();

    public PathFinder(PathFinderStrategy strategy) {
        this.strategyGraph = new GraphBuilder(strategy);
    }

    public Path findPath(PathFinderRequest request) {
        validIsSameOriginStation(request.getSourceStation(), request.getTargetStation());
        WeightedMultigraph<Station, SectionEdge> pathSectionGraph = strategyGraph.getGraph(request.getSections());
        List<Section> searchedSections = strategyGraph.getPath(pathSectionGraph, request.getSourceStation(), request.getTargetStation());

        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.from(request);
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
