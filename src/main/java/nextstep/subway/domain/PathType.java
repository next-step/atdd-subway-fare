package nextstep.subway.domain;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public enum PathType {
    DISTANCE {
        @Override
        protected void setEdgeWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section section) {
            SectionEdge sectionEdge = SectionEdge.of(section);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
    },
    DURATION {
        @Override
        protected void setEdgeWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section section) {
            SectionEdge sectionEdge = SectionEdge.of(section);
            graph.setEdgeWeight(sectionEdge, section.getDuration());
        }
    },
    ;

    public void registerEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    setEdgeWeight(graph, it);
                });
    }

    protected abstract void setEdgeWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section section);

    public static PathType of(String codeStr) {
        if (codeStr == null) {
            throw new IllegalArgumentException("잘못된 타입입니다.");
        }
        String code = codeStr.toUpperCase();
        return Arrays.stream(values())
                .filter(pathType -> pathType.name().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 타입입니다."));
    }
}
