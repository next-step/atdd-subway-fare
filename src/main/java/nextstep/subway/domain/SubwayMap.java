package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubwayMap implements PathFinder {
	private final WeightedMultigraph<Long, SectionWeightedEdge> weightedMultigraph;

	public SubwayMap() {
		weightedMultigraph = new WeightedMultigraph<>(SectionWeightedEdge.class);
	}

	public WeightedMultigraph<Long, SectionWeightedEdge> getWeightedMultigraph() {
		return weightedMultigraph;
	}

	@Override
	public Path getShortestPath(Long source, Long target) {
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath<>(weightedMultigraph);
		GraphPath graphPath;

		try {
			graphPath = dijkstraShortestPath.getPath(source, target);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("경로에 존재하지 않는 역입니다.");
		}

		if(Objects.isNull(graphPath)) {
			throw new IllegalArgumentException("경로가 존재하지 않습니다.");
		}

		List<SectionWeightedEdge> edges = graphPath.getEdgeList();

		return new Path(graphPath.getVertexList(),
				edges.stream()
						.map(SectionWeightedEdge::getSection)
						.collect(Collectors.toList()));
	}
}
