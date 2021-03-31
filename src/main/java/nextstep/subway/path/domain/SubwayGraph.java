package nextstep.subway.path.domain;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.exception.DoesNotConnectedPathException;
import nextstep.subway.path.exception.SameStationPathSearchException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubwayGraph {

    private final WeightedMultigraph<Station, SubwayEdge> subwayEdgeGraph;
    private final GraphPath<Station, SubwayEdge> subwayGraph;

    public SubwayGraph(List<Section> sections, PathType type, Station sourceStation, Station targetStation) {
        validateSourceAndTargetStation(sourceStation, targetStation);

        subwayEdgeGraph = new WeightedMultigraph<>(SubwayEdge.class);

        subwayGraph = initShortestPathGraph(sections, type, sourceStation, targetStation);
    }

    private void validateSourceAndTargetStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new SameStationPathSearchException();
        }
    }

    private GraphPath<Station, SubwayEdge> initShortestPathGraph(List<Section> sections, PathType type, Station source, Station target) {
        Set<Station> stations = getDistinctStations(sections);

        addVertex(stations);
        addEdge(sections, type);

        DijkstraShortestPath<Station, SubwayEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayEdgeGraph);
        validateConnectPathStation(stations, source, target);
        return dijkstraShortestPath.getPath(source, target);
    }

    private Set<Station> getDistinctStations(List<Section> sections) {
        Set<Station> stations = new HashSet<>();

        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }

    private void addVertex(Set<Station> stations) {
        for (Station station : stations) {
            subwayEdgeGraph.addVertex(station);
        }
    }

    private void addEdge(List<Section> sections, PathType type) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            SubwayEdge subwayEdge = subwayEdgeGraph.addEdge(upStation, downStation);
            subwayEdge.addSection(section);
            type.findWeightOf(section);
        }
    }

    private void validateConnectPathStation(Set<Station> stations, Station source, Station target) {
        if (!stations.contains(source) || !stations.contains(target)) {
            throw new DoesNotConnectedPathException();
        }
    }

    public PathResult findPath() {
        List<Section> sections = subwayGraph.getEdgeList().stream()
                .map(SubwayEdge::getSection)
                .collect(Collectors.toList());

        return new PathResult(new Stations(subwayGraph.getVertexList()), new Sections(sections));
    }
}
