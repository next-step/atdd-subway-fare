package nextstep.subway.domain;

import nextstep.subway.common.constant.PathType;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {

    private List<Line> lines;
    SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
    private String findType;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
        this.findType = PathType.DISTANCE.getName();
        initPath();
    }

    public SubwayMap(List<Line> lines, String findType) {
        this.lines = lines;
        this.findType = findType;
        initPath();
    }

    public Path findPath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private void initPath() {
        addVertex();
        addEdge();;
    }

    private void addVertex() {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));
    }

    private void addEdge() {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, getWeightByFindType(it));
                });

        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                        it.getLine(),
                        it.getDownStation(),
                        it.getUpStation(),
                        it.getDistance(),
                        it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, getWeightByFindType(it));
                });
    }

    private int getWeightByFindType(Section section) {
        if (findType.equals(PathType.DURATION.getName())) { return section.getDuration(); }
        return section.getDistance();
    }
}