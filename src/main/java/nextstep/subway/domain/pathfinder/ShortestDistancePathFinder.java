package nextstep.subway.domain.pathfinder;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.CustomWeightedEdge;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class ShortestDistancePathFinder extends PathFinder {
    @Override
    public boolean isType(final PathType pathType) {
        return PathType.DISTANCE == pathType;
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
        return new PathResponse(path.getVertexList(), (int) path.getWeight(), duration);
    }
}


