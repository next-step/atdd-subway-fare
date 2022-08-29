package nextstep.subway.domain.SubwayMap;

import nextstep.subway.domain.ArrivalTime;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.Multigraph;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArrivalTimeSubwayMap extends SubwayMap {

    public ArrivalTimeSubwayMap(List<Line> lines) {
        super(lines);
    }

    @Override
    public Path findPath(Station source, Station target, int age, String time) {
        Path path = super.findPath(source, target, age, time);
        ArrivalTime arrivalTime = new ArrivalTime(path.getSections(), lines, time);
        return new Path(path, arrivalTime);
    }

    @Override
    protected GraphPath<Station, SectionEdge> getGraphPath(Station source, Station target) {
        KShortestPaths kShortestPaths = new KShortestPaths(getGraph(), 100);
        List<GraphPath> paths = kShortestPaths.getPaths(source, target);
        return paths.stream()
                .min(Comparator.comparing(path -> path.getWeight()))
                .orElse(null);
    }

    private Multigraph<Station, SectionEdge> getGraph() {
        Multigraph<Station, SectionEdge> graph = new Multigraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                                it.getLine(),
                                it.getDownStation(),
                                it.getUpStation(),
                                it.getDistance(),
                                it.getDuration()
                        )
                )
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });
        return graph;
    }
}
