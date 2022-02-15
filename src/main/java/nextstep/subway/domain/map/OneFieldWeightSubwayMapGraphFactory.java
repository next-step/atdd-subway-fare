package nextstep.subway.domain.map;

import java.util.List;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;

public class OneFieldWeightSubwayMapGraphFactory extends SubwayMapGraphFactory {
    private final Function<Section, Double> getFieldStrategy;

    public OneFieldWeightSubwayMapGraphFactory(Function<Section, Double> getFieldStrategy) {
        this.getFieldStrategy = getFieldStrategy;
    }

    @Override
    protected void addEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
             .flatMap(it -> it.getSections().stream())
             .forEach(it -> {
                 SectionEdge sectionEdge = SectionEdge.of(it);
                 graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                 graph.setEdgeWeight(sectionEdge, getFieldStrategy.apply(it));
             });
    }
}
