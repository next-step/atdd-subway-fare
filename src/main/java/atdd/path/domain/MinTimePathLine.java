package atdd.path.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public class MinTimePathLine {
    private Line line;
    private List<Edge> edges;

    private MinTimePathLine(Line line, List<Edge> edges) {
        this.line = line;
        this.edges = edges;
    }

    public static List<MinTimePathLine> listOf(List<Line> lines, List<Station> pathStations) {
        List<MinTimePathLine> minTimePathLines = new ArrayList<>();

        for (int i = 0; i < pathStations.size() - 1; i++) {
            for (Line line : lines) {
                Optional<Edge> edgeOptional = findEdge(line.getEdges(), pathStations.get(i).getId(), pathStations.get(i + 1).getId());

                if (!edgeOptional.isPresent()) {
                    continue;
                }

                Optional<MinTimePathLine> minTimePathLine = findMinTimePathLine(minTimePathLines, line.getId());

                if (!minTimePathLine.isPresent()) {
                    minTimePathLines.add(new MinTimePathLine(line, new ArrayList(Arrays.asList(edgeOptional.get()))));
                    break;
                }

                minTimePathLine.get().addEdge(edgeOptional.get());

                break;
            }
        }

        return minTimePathLines;
    }

    private void addEdge(Edge edge) {
        edges.add(edge);
    }

    private static Optional<Edge> findEdge(List<Edge> edges, long sourceId, long endId) {
        return edges.stream()
                .filter(it -> it.isSourceStation(sourceId) && it.isTargetStation(endId))
                .findFirst();
    }

    private static Optional<MinTimePathLine> findMinTimePathLine(List<MinTimePathLine> minTimePathLines, long lineId) {
        return minTimePathLines.stream()
                .filter(it -> it.getLine().getId() == lineId)
                .findFirst();
    }
}
