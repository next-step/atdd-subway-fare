package nextstep.subway.domain.SubwayMap;

import nextstep.subway.domain.ArrivalTime;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.Multigraph;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ArrivalTimeSubwayMap extends SubwayMap {

    public static final int MAX_PASS_STATION_COUNT = 100;

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
        KShortestPaths kShortestPaths = new KShortestPaths(
                getGraph(() -> new Multigraph<>(SectionEdge.class)), MAX_PASS_STATION_COUNT);
        List<GraphPath> paths = kShortestPaths.getPaths(source, target);
        return paths.stream()
                .min(Comparator.comparing(path -> path.getWeight()))
                .orElse(null);
    }

    @Override
    protected Consumer<Section> getConnectSectionConsumer(AbstractBaseGraph<Station, SectionEdge> graph) {
        return it -> {
            SectionEdge sectionEdge = SectionEdge.of(it);
            graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, it.getDuration());
        };
    }

}
