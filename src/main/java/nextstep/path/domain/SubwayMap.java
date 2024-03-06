package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.path.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SubwayMap {
    private final List<Line> lines;
    private final Map<PathType, WeightedMultigraph<Station, PathSection>> graphCache;

    public SubwayMap(final List<Line> lines) {
        this.lines = lines;
        this.graphCache = Arrays.stream(PathType.values())
                .collect(Collectors.toMap(Function.identity(), this::buildGraph));
    }

    private WeightedMultigraph<Station, PathSection> buildGraph(final PathType pathType) {
        final WeightedMultigraph<Station, PathSection> graph = new WeightedMultigraph<>(PathSection.class);
        lines.forEach(line -> initGraph(line, pathType, graph));
        return graph;
    }

    private void initGraph(final Line line, final PathType pathType, final WeightedMultigraph<Station, PathSection> graph) {
        line.getSections().forEach(section -> {
            final PathSection pathSection = PathSection.from(line, section);
            final Station upStation = pathSection.getUpStation();
            final Station downStation = pathSection.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.addEdge(upStation, downStation, pathSection);
            graph.setEdgeWeight(pathSection, pathType.calculateWeight(pathSection));
        });
    }

    public Optional<Path> findShortestPath(final Station sourceStation, final Station targetStation, final PathType pathType) {
        return Optional.ofNullable(getShortestPath(sourceStation, targetStation, pathType))
                .map(shortestPath -> new Path(shortestPath.getVertexList(), shortestPath.getEdgeList()));
    }

    private GraphPath<Station, PathSection> getShortestPath(final Station sourceStation, final Station targetStation, final PathType pathType) {
        final WeightedMultigraph<Station, PathSection> graph = graphCache.get(pathType);
        if (!(graph.containsVertex(sourceStation) && graph.containsVertex(targetStation))) {
            throw new PathNotFoundException();
        }

        return new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);
    }

}
