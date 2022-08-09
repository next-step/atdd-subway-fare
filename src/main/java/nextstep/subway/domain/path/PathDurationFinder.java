package nextstep.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

public class PathDurationFinder implements PathFinder {
	private List<Line> lines;

	public PathDurationFinder(List<Line> lines) {
		this.lines = lines;
	}

	@Override
	public Path findPath(Station source, Station target) {
		SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
		addVertex(graph);
		addEdge(graph);
		addEdgeOpposite(graph);
		// 다익스트라 최단 경로 찾기
		DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

		List<Section> sections = result.getEdgeList().stream()
			.map(it -> it.getSection())
			.collect(Collectors.toList());
		return new Path(new Sections(sections));
	}

	// 지하철 역(정점)을 등록
	private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
		this.lines
			.stream()
			.flatMap(it -> it.getStations().stream())
			.distinct()
			.collect(Collectors.toList())
			.forEach(graph::addVertex);
	}

	// 지하철 역의 연결 정보(간선)을 등록
	private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
		this.lines
			.stream()
			.flatMap(it -> it.getSections().stream())
			.forEach(it -> {
				SectionEdge sectionEdge = SectionEdge.of(it);
				graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
				graph.setEdgeWeight(sectionEdge, it.getDuration());
			});
	}

	// 지하철 역의 연결 정보(간선)을 등록
	private void addEdgeOpposite(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
		this.lines
			.stream()
			.flatMap(it -> it.getSections().stream())
			.map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(),
				it.getDuration()))
			.forEach(it -> {
				SectionEdge sectionEdge = SectionEdge.of(it);
				graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
				graph.setEdgeWeight(sectionEdge, it.getDuration());
			});
	}
}
