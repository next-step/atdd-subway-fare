package nextstep.subway.domain.SubwayMap;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotConnectSectionException;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.AbstractBaseGraph;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubwayMap {
    protected List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, int age, String time) {
        // 다익스트라 최단 경로 찾기
        GraphPath<Station, SectionEdge> result = getGraphPath(source, target);

        validateConnectSection(result);

        Sections sections = new Sections(streamSections(result).collect(Collectors.toList()));

        int maxAdditionalFare = streamSections(result)
                .mapToInt(section -> section.getLine().getAdditionalFare())
                .max()
                .orElse(0);

        return new Path(sections, new Fare(sections.totalDistance(), maxAdditionalFare, age));
    }

    protected void validateConnectSection(GraphPath<Station, SectionEdge> result) {
        if (result == null) {
            throw new NotConnectSectionException();
        }
    }

    protected Stream<Section> streamSections(GraphPath<Station, SectionEdge> result) {
        return result.getEdgeList().stream()
                .map(SectionEdge::getSection);
    }

    protected AbstractBaseGraph<Station, SectionEdge> getGraph(Supplier<AbstractBaseGraph<Station, SectionEdge>> graphSupplier) {
        AbstractBaseGraph<Station, SectionEdge> graph = graphSupplier.get();
        setupStations(graph);
        connectSections(getConnectSectionConsumer(graph));
        return graph;
    }

    protected void setupStations(Graph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));
    }

    protected void connectSections(Consumer<Section> sectionConsumer) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(sectionConsumer);

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
                .forEach(sectionConsumer);
    }

    protected abstract GraphPath<Station, SectionEdge> getGraphPath(Station source, Station target);

    protected abstract Consumer<Section> getConnectSectionConsumer(AbstractBaseGraph<Station, SectionEdge> graph);
}
