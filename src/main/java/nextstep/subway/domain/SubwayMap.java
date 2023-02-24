package nextstep.subway.domain;

import static nextstep.subway.applicaion.dto.SearchType.*;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SearchType;

@RequiredArgsConstructor
public class SubwayMap {

    private final List<Line> lines;

    public Path findPath(Station source, Station target, SearchType type) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> SectionEdge(graph, it, type));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> SectionEdge(graph, it, type));

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

	private void SectionEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section it, SearchType type) {
		SectionEdge sectionEdge = SectionEdge.of(it);
		graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
		graph.setEdgeWeight(sectionEdge, getWeightByType(it, type));
	}

	private int getWeightByType(Section section, SearchType type) {
		if (type.equals(DISTANCE)) {
			return section.getDistance();
		}
		return section.getDuration();
	}
}
