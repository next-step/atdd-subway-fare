package nextstep.subway.domain;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.Arrays;
import java.util.List;

public enum PathType {
    DISTANCE {
        @Override
        public void registerEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
            lines.stream()
                    .flatMap(it -> it.getSections().stream())
                    .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                    .forEach(it -> {
                        SectionEdge sectionEdge = SectionEdge.of(it);
                        graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                        graph.setEdgeWeight(sectionEdge, it.getDistance());
                    });
        }
    },
    DURATION {
        @Override
        public void registerEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
            lines.stream()
                    .flatMap(it -> it.getSections().stream())
                    .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                    .forEach(it -> {
                        SectionEdge sectionEdge = SectionEdge.of(it);
                        graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                        graph.setEdgeWeight(sectionEdge, it.getDuration());
                    });
        }
    },
    ;

    public abstract void registerEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph);

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
