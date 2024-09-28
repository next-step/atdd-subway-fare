package nextstep.subway.domain.path;

import java.util.List;
import java.util.stream.Stream;

import nextstep.subway.domain.section.Section;
import org.springframework.stereotype.Component;

@Component
public class LeastDistanceFinder extends PathFinder {

    @Override
    void setUpWeightedEdges(List<Section> edges) {
        edges.stream()
                .flatMap(edge -> Stream.of(edge.getUpwardStation(), edge.getDownwardStation()))
                .distinct()
                .forEach(graph::addVertex);

        edges.forEach(edge -> {
            SectionEdge sectionEdge = graph.addEdge(edge.getUpwardStation(), edge.getDownwardStation());
            sectionEdge.addSection(edge);
            graph.setEdgeWeight(sectionEdge, edge.getDistance());
        });
    }

    @Override
    Long getDistance() {
        List<SectionEdge> edgeList = path.getEdgeList();

        if (edgeList.isEmpty()) {
            return null;
        }

        return (long) path.getWeight();
    }

    @Override
    Long getTransitTime() {
        List<SectionEdge> edgeList = path.getEdgeList();

        if (edgeList.isEmpty()) {
            return null;
        }

        return edgeList.stream()
                .mapToLong(sectionEdge -> sectionEdge.getSection().getTransitTime())
                .sum();
    }

    @Override
    Long getFare() {
        List<SectionEdge> edgeList = path.getEdgeList();

        if (edgeList.isEmpty()) {
            return null;
        }

        return calculateFare((long) path.getWeight());
    }
}
