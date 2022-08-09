package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
	private List<Line> lines;

	public SubwayMap(List<Line> lines) {
		this.lines = lines;
	}

	public Path findPath(Station source, Station target, PathType type) {
		SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

		setVertex(graph);
		setVertexInfo(type, graph);

		// 다익스트라 최단 경로 찾기
		DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

		List<Section> sections = result.getEdgeList().stream()
				.map(it -> it.getSection())
				.collect(Collectors.toList());

		return new Path(new Sections(sections));
	}

	private void setVertexInfo(PathType type, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
		lines.stream()
				.flatMap(it -> it.getSections().stream())
				.forEach(it -> {
					SectionEdge sectionEdge = SectionEdge.of(it);
					graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
					if (isPathTypeDistance(type)) {
						graph.setEdgeWeight(sectionEdge, it.getDistance());
						return;
					}
					graph.setEdgeWeight(sectionEdge, it.getDuration());
				});

		lines.stream()
				.flatMap(it -> it.getSections().stream())
				.map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
				.forEach(it -> {
					SectionEdge sectionEdge = SectionEdge.of(it);
					graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
					if (isPathTypeDistance(type)) {
						graph.setEdgeWeight(sectionEdge, it.getDistance());
						return;
					}
					graph.setEdgeWeight(sectionEdge, it.getDuration());
				});
	}

	private boolean isPathTypeDistance(PathType type) {
		return type.equals(PathType.DISTANCE);
	}

	private void setVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
		lines.stream()
				.flatMap(it -> it.getStations().stream())
				.distinct()
				.collect(Collectors.toList())
				.forEach(it -> graph.addVertex(it));
	}
}
