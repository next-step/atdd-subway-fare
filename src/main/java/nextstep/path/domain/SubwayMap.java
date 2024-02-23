package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.path.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class SubwayMap {
    private final List<Line> lines;

    public SubwayMap(final List<Line> lines) {
        this.lines = lines;
    }

    public Optional<Path> findShortestPath(final Station sourceStation, final Station targetStation, final PathType pathType) {
        final DijkstraShortestPath<Station, PathSection> path = getShortestPath(sourceStation, targetStation, pathType);

        return Optional.ofNullable(path.getPath(sourceStation, targetStation))
                .map(shortestPath -> new Path(shortestPath.getVertexList(), shortestPath.getEdgeList()));
    }

    private DijkstraShortestPath<Station, PathSection> getShortestPath(final Station sourceStation, final Station targetStation, final PathType pathType) {
        final WeightedMultigraph<Station, PathSection> graph = buildGraph(pathType);

        if (!(graph.containsVertex(sourceStation) && graph.containsVertex(targetStation))) {
            throw new PathNotFoundException();
        }

        return new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, PathSection> buildGraph(final PathType pathType) {
        final WeightedMultigraph<Station, PathSection> graph = new WeightedMultigraph<>(PathSection.class);
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .map(PathSection::from)
                .forEach(pathSection -> initGraph(pathSection, pathType, graph));
        return graph;
    }

    private void initGraph(final PathSection pathSection, final PathType pathType, final WeightedMultigraph<Station, PathSection> graph) {
        final Station upStation = pathSection.getUpStation();
        final Station downStation = pathSection.getDownStation();
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.addEdge(upStation, downStation, pathSection);
        graph.setEdgeWeight(pathSection, pathType.calculateWeight(pathSection));
    }
}
