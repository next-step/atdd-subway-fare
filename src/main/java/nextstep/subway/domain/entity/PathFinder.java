package nextstep.subway.domain.entity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nextstep.subway.domain.enums.PathSearchType;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

@Slf4j
public class PathFinder {

    @Getter
    static class SectionEdge extends DefaultWeightedEdge {

        private final Section section;

        SectionEdge(Section section) {
            this.section = section;
        }
    }

    private final List<Line> lines;
    private final DijkstraShortestPath<Station, SectionEdge> distanceShortestPath;
    private final DijkstraShortestPath<Station, SectionEdge> durationShortestPath;

    public PathFinder(List<Line> lines) {
        this.lines = lines;
        this.distanceShortestPath = new DijkstraShortestPath<>(createWeightedGraph(PathSearchType.DISTANCE));
        this.durationShortestPath = new DijkstraShortestPath<>(createWeightedGraph(PathSearchType.DURATION));
    }

    public Optional<Path> findShortestPath(Station source, Station target, PathSearchType type) {
        if (source == target) {
            throw new IllegalArgumentException("Source and target stations are the same");
        }
        try {
            GraphPath<Station, SectionEdge> result = getShortedPath(source, target, type);
            List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
            return Optional.of(new Path(new Sections(sections)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isValidPath(Station source, Station target) {
        try {
            Optional<Path> path = findShortestPath(source, target, PathSearchType.DISTANCE);
            return path.isPresent();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private GraphPath<Station, SectionEdge> getShortedPath(Station source, Station target,
        PathSearchType type) {
        if (type == PathSearchType.DISTANCE) {
            return distanceShortestPath.getPath(source, target);
        }
        if (type == PathSearchType.DURATION) {
            return durationShortestPath.getPath(source, target);
        }
        throw new IllegalStateException("invalid PathSearchType type");
    }

    private WeightedGraph<Station, SectionEdge> createWeightedGraph(PathSearchType type) {
        SimpleWeightedGraph<Station, SectionEdge> graph = new SimpleWeightedGraph<>(SectionEdge.class);
        addStationsAsVerticesToGraph(graph);
        addSectionsAsWeightedEdgeToGraph(graph, type);
        return graph;
    }

    private void addStationsAsVerticesToGraph(Graph<Station, SectionEdge> graph) {
        this.lines.stream()
            .flatMap(line -> line.getAllStationsByDistinct().stream())
            .forEach(graph::addVertex);
    }

    private void addSectionsAsWeightedEdgeToGraph(WeightedGraph<Station, SectionEdge> graph,
        PathSearchType type) {
        this.lines.stream()
            .flatMap(line -> line.getSections().getAllSections().stream())
            .forEach(section -> {
                SectionEdge sectionEdge = new SectionEdge(section);
                graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, getSectionEdgeWeight(section, type));
            });
    }

    private long getSectionEdgeWeight(Section section, PathSearchType type) {
        if (type == PathSearchType.DISTANCE) {
            return section.getDistance();
        }
        if (type == PathSearchType.DURATION) {
            return section.getDuration();
        }
        throw new IllegalStateException("invalid PathSearchType type");
    }
}
