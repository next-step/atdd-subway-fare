package nextstep.subway.domain;

import nextstep.subway.domain.entity.Section;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public abstract class PathFinder {
	protected WeightedMultigraph<Long, SectionWeightedEdge> weightedMultigraph;
	protected DijkstraShortestPath dijkstraShortestPath;

	public PathFinder(List<Section> sections) {
		weightedMultigraph = new WeightedMultigraph(SectionWeightedEdge.class);

		sections.forEach(this::addPath);

		dijkstraShortestPath = new DijkstraShortestPath<>(weightedMultigraph);
	}

	public Path getPath(Long source, Long target) {
		return new Path(dijkstraShortestPath, source, target);
	}

	protected abstract void addPath(Section section);
}
