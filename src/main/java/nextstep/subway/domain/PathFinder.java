package nextstep.subway.domain;

import nextstep.subway.domain.entity.Section;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public abstract class PathFinder {
	protected WeightedMultigraph<Long, DefaultWeightedEdge> weightedMultigraph;
	protected DijkstraShortestPath dijkstraShortestPath;

	public PathFinder(List<Section> sections) {
		weightedMultigraph = new WeightedMultigraph(DefaultWeightedEdge.class);

		sections.forEach(this::addPath);

		dijkstraShortestPath = new DijkstraShortestPath<>(weightedMultigraph);
	}

	public List<Long> getPath(Long source, Long target) {
		GraphPath graphPath;

		try {
			graphPath = dijkstraShortestPath.getPath(source, target);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("경로에 존재하지 않는 역입니다.");
		}

		if(Objects.isNull(graphPath)) {
			throw new IllegalArgumentException("경로가 존재하지 않습니다.");
		}

		return graphPath.getVertexList();
	}

	public double getWieght(Long source, Long target) {
		return dijkstraShortestPath.getPathWeight(source, target);
	}

	protected abstract void addPath(Section section);
}
