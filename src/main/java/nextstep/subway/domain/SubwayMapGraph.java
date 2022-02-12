package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMapGraph {

	private final SimpleDirectedWeightedGraph<Station, SectionEdge> graph;
	private final PathType pathType;

	public SubwayMapGraph(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, PathType pathType) {
		this.graph = graph;
		this.pathType = pathType;
	}

	public static SubwayMapGraph createGraph(List<Line> lines, PathType pathType) {
		SubwayMapGraph subwayMapGraph
				= new SubwayMapGraph(new SimpleDirectedWeightedGraph<>(SectionEdge.class), pathType);

		subwayMapGraph.addVertex(lines);
		subwayMapGraph.addEdge(lines);
		subwayMapGraph.addOppositeEdge(lines);

		return subwayMapGraph;
	}

	private void addVertex(List<Line> lines) {
		// 지하철 역(정점)을 등록
		lines.stream()
				.flatMap(it -> it.getStations().stream())
				.distinct()
				.collect(Collectors.toList())
				.forEach(graph::addVertex);
	}

	private void addEdge(List<Line> lines) {
		// 지하철 역의 연결 정보(간선)을 등록
		lines.stream()
				.flatMap(it -> it.getSections().stream())
				.forEach(it -> {
					SectionEdge sectionEdge = SectionEdge.of(it);
					graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
					graph.setEdgeWeight(sectionEdge, pathType.getWeight(it));
				});
	}

	private void addOppositeEdge(List<Line> lines) {
		// 지하철 역의 연결 정보(간선)을 등록
		lines.stream()
				.flatMap(it -> it.getSections().stream())
				.map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
				.forEach(it -> {
					SectionEdge sectionEdge = SectionEdge.of(it);
					graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
					graph.setEdgeWeight(sectionEdge, pathType.getWeight(it));
				});
	}

	public List<Section> getShortestPathSections(Station source, Station target) {
		return getShortestPath(source, target)
				.getEdgeList().stream()
				.map(SectionEdge::getSection)
				.collect(Collectors.toList());
	}

	private GraphPath<Station, SectionEdge> getShortestPath(Station source, Station target) {
		return new DijkstraShortestPath<>(graph).getPath(source, target);
	}

}
