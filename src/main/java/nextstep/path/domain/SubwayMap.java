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

    public Optional<Path> findShortestDistancePath(final Station sourceStation, final Station targetStation) {
        final DijkstraShortestPath<Station, PathSection> path = getShortestDistancePath(sourceStation, targetStation);

        return Optional.ofNullable(path.getPath(sourceStation, targetStation))
                .map(shortestPath -> new Path(shortestPath.getVertexList(), shortestPath.getEdgeList()));
    }

    public Optional<Path> findShortestDurationPath(final Station sourceStation, final Station targetStation) {
        final DijkstraShortestPath<Station, PathSection> path = getShortestDurationPath(sourceStation, targetStation);

        return Optional.ofNullable(path.getPath(sourceStation, targetStation))
                .map(shortestPath -> new Path(shortestPath.getVertexList(), shortestPath.getEdgeList()));
    }

    private DijkstraShortestPath<Station, PathSection> getShortestDurationPath(final Station sourceStation, final Station targetStation) {
        final WeightedMultigraph<Station, PathSection> graph = buildGraphForDuration();

        if (!(graph.containsVertex(sourceStation) && graph.containsVertex(targetStation))) {
            throw new PathNotFoundException();
        }

        return new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, PathSection> buildGraphForDuration() {
        final WeightedMultigraph<Station, PathSection> graph = new WeightedMultigraph<>(PathSection.class);
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .map(PathSection::from)
                .forEach(pathSection -> initGraphForDuration(pathSection, graph));
        return graph;
    }

    private void initGraphForDuration(final PathSection pathSection, final WeightedMultigraph<Station, PathSection> graph) {
        final Station upStation = pathSection.getUpStation();
        final Station downStation = pathSection.getDownStation();
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.addEdge(upStation, downStation, pathSection);
        graph.setEdgeWeight(pathSection, pathSection.getDuration());
    }

    private DijkstraShortestPath<Station, PathSection> getShortestDistancePath(final Station sourceStation, final Station targetStation) {
        final WeightedMultigraph<Station, PathSection> graph = buildGraph();

        if (!(graph.containsVertex(sourceStation) && graph.containsVertex(targetStation))) {
            throw new PathNotFoundException();
        }

        return new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, PathSection> buildGraph() {
        final WeightedMultigraph<Station, PathSection> graph = new WeightedMultigraph<>(PathSection.class);
        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .map(PathSection::from)
                .forEach(pathSection -> initGraph(pathSection, graph));
        return graph;
    }

    private void initGraph(final PathSection pathSection, final WeightedMultigraph<Station, PathSection> graph) {
        final Station upStation = pathSection.getUpStation();
        final Station downStation = pathSection.getDownStation();
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.addEdge(upStation, downStation, pathSection);
        graph.setEdgeWeight(pathSection, pathSection.getDistance());
    }
}
