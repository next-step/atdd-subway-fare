package nextstep.subway.domain.path;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.CustomWeightedEdge;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class ShortestDistancePathFinder extends PathFinder {

    public ShortestDistancePathFinder(final CalculateHandler calculateHandler) {
        super(calculateHandler);
    }

    @Override
    protected WeightedMultigraph<Station, CustomWeightedEdge> createGraph(final List<Section> sections) {
        WeightedMultigraph<Station, CustomWeightedEdge> graph = new WeightedMultigraph<>(CustomWeightedEdge.class);

        sections.forEach(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();
                    graph.addVertex(downStation);
                    graph.addVertex(upStation);
                    final CustomWeightedEdge customWeightedEdge = graph.addEdge(upStation, downStation);
                    customWeightedEdge.addDuration(section.getDuration());
                    graph.setEdgeWeight(customWeightedEdge, section.getDistance());
                });

        return graph;
    }

    @Override
    protected PathResponse createPathResponse(final GraphPath<Station, CustomWeightedEdge> path) {
        final int duration = path.getEdgeList().stream()
                .mapToInt(CustomWeightedEdge::getDuration)
                .sum();

        final int weight = (int) path.getWeight();

        return new PathResponse(path.getVertexList(), weight, duration, calculateFare(weight));
    }
}


