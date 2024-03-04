package nextstep.subway.domain.path;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.CustomWeightedEdge;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.fee.CalculateHandler;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestDistancePathFinder extends PathFinder {

    public ShortestDistancePathFinder(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    protected WeightedMultigraph<Station, CustomWeightedEdge> createGraph(final List<Line> lines) {
        final List<Section> sections = getSections(lines);

        WeightedMultigraph<Station, CustomWeightedEdge> graph = new WeightedMultigraph<>(CustomWeightedEdge.class);

        sections.forEach(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();
                    graph.addVertex(downStation);
                    graph.addVertex(upStation);
                    final CustomWeightedEdge customWeightedEdge = graph.addEdge(upStation, downStation);
                    customWeightedEdge.addDuration(section.getDuration());
                    customWeightedEdge.addAdditionalFee(section.getLine().getAddtionalFee());
                    graph.setEdgeWeight(customWeightedEdge, section.getDistance());
                });

        return graph;
    }

    private static List<Section> getSections(final List<Line> lines) {
        return lines.stream()
                .flatMap(l -> l.getSections().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    protected PathResponse createPathResponse(final GraphPath<Station, CustomWeightedEdge> path) {
        final int duration = path.getEdgeList().stream()
                .mapToInt(CustomWeightedEdge::getDuration)
                .sum();

        final int weight = (int) path.getWeight();

        final List<Integer> feeList = path.getEdgeList().stream()
                .map(CustomWeightedEdge::getAdditionalFee)
                .collect(Collectors.toList());

        return new PathResponse(path.getVertexList(), weight, duration, calculateFare(weight, feeList));
    }
}


