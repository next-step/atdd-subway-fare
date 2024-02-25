package nextstep.subway.domain.pathfinder;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.CustomWeightedEdge;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShortestDurationPathFinder extends PathFinder {
    @Override
    public boolean isType(final PathType pathType) {
        return PathType.DURATION == pathType;
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
            customWeightedEdge.addDistance(section.getDistance());
            graph.setEdgeWeight(customWeightedEdge, section.getDuration());
        });

        return graph;
    }

    @Override
    protected PathResponse createPathResponse(final GraphPath<Station, CustomWeightedEdge> path) {
        final int distance = path.getEdgeList().stream()
                .mapToInt(CustomWeightedEdge::getDistance)
                .sum();
        return new PathResponse(path.getVertexList(), distance, (int) path.getWeight(), 0);
    }
}
