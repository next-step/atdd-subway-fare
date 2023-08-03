package subway.path.application;

import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.domain.Path;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathFinder {

    private final GraphBuilder graph;
    private final PathFare pathFare = new PathFare();

    public PathFinder(PathStrategy strategy) {
        this.graph = new GraphBuilder(strategy);
    }

    public Path findPath(List<Section> sections, Station sourceStation, Station targetStation) {
        validIsSameOriginStation(sourceStation, targetStation);
        WeightedMultigraph<Station, SectionEdge> graph = this.graph.getGraph(sections);
        List<Section> sectionsInPath = this.graph.getPath(graph, sourceStation, targetStation);

        Long totalDistance = getTotalDistanceInPath(sectionsInPath);
        Long totalDuration = getTotalDurationInPath(sectionsInPath);
        long totalFareByDistance = pathFare.calculateFare(sections, sourceStation, targetStation);
        List<Station> stationsInPath = getStations(sectionsInPath);

        return Path.builder().totalDistance(totalDistance)
                .totalDuration(totalDuration)
                .totalFare(totalFareByDistance)
                .stations(stationsInPath)
                .build();
    }


    private void validIsSameOriginStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_REQUEST_STATION_IS_SAME_ORIGIN);
        }
    }

    private Long getTotalDurationInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDuration)
                .reduce(0L, Long::sum);
    }

    private Long getTotalDistanceInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }

    private List<Station> getStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }
}
