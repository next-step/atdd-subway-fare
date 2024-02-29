package nextstep.api.subway.infrastructure.operators;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Component;

import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.model.entity.Station;
import nextstep.api.subway.domain.model.vo.Path;
import nextstep.api.subway.domain.operators.PathFinder;
import nextstep.common.exception.subway.PathNotValidException;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */

@Component
public class DijkstraBaseMinimunDurationPathFinder implements PathFinder {

	@Override
	public Path findShortestPathBySections(Station sourceStation, Station targetStation, List<Section> sections) {
		Graph<Station, DefaultWeightedEdge> graph = createGraph(sections);

		GraphPath<Station, DefaultWeightedEdge> shortestPath = calculateShortestPath(sourceStation, targetStation, graph).orElseThrow(
			() -> new PathNotValidException("No path exists between the source and target stations."));

		return Path.of(fetchStationsInPath(shortestPath),  null, calculateTotalDistance(graph, shortestPath, sections), calculateTotalDuration(graph, shortestPath));
	}

	@Override
	public Path findShortestPathByLines(Station sourceStation, Station targetStation, List<Line> lines) {
		List<Section> sections = fetchSections(sourceStation, targetStation, lines);
		Graph<Station, DefaultWeightedEdge> graph = createGraph(sections);

		GraphPath<Station, DefaultWeightedEdge> shortestPath = calculateShortestPath(sourceStation, targetStation, graph).orElseThrow(
			() -> new PathNotValidException("No path exists between the source and target stations."));
		List<Line> linesTraversed = determineLinesTraversed(shortestPath, sections, lines);

		return Path.of(fetchStationsInPath(shortestPath),  linesTraversed, calculateTotalDistance(graph, shortestPath, sections), calculateTotalDuration(graph, shortestPath));
	}


	private  List<Section> fetchSections(Station sourceStation, Station targetStation, List<Line> lines) {
		return lines.stream()
			.filter(line -> line.isContainsAnyStation(sourceStation.getId(), targetStation.getId()))
			.map(Line::parseSections)
			.flatMap(java.util.Collection::stream)
			.collect(Collectors.toList());
	}


	private static Optional<GraphPath<Station, DefaultWeightedEdge>> calculateShortestPath(Station sourceStation, Station targetStation, Graph<Station, DefaultWeightedEdge> graph) {
		try {
			return Optional.ofNullable(new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation));
		} catch (Exception e) {
			throw new PathNotValidException("Shortest Path finding algorithm not supported");
		}
	}

	private Graph<Station, DefaultWeightedEdge> createGraph(List<Section> sections) {
		Graph<Station, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		sections.forEach(section -> {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			Graphs.addEdgeWithVertices(graph, section.getUpStation(), section.getDownStation(), section.getDuration());
		});
		return graph;
	}

	private long calculateTotalDistance(Graph<Station, DefaultWeightedEdge> graph, GraphPath<Station, DefaultWeightedEdge> shortestPath, List<Section> sections) {
		return shortestPath.getEdgeList().stream()
			.mapToLong(edge -> {
				Station source = graph.getEdgeSource(edge);
				Station target = graph.getEdgeTarget(edge);
				return findSectionDistance(source, target, sections);
			})
			.sum();
	}

	private long findSectionDistance(Station source, Station target, List<Section> sections) {
		return sections.stream()
			.filter(section -> section.isSameUpStation(source) && section.isSameDownStation(target))
			.findFirst()
			.map(Section::getDistance)
			.orElse(0L);
	}

	private long calculateTotalDuration(Graph<Station, DefaultWeightedEdge> graph, GraphPath<Station, DefaultWeightedEdge> shortestPath) {
		return (long)shortestPath.getEdgeList().stream()
			.mapToDouble(graph::getEdgeWeight)
			.sum();
	}

	private List<Station> fetchStationsInPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
		return shortestPath.getVertexList();
	}


	private List<Line> determineLinesTraversed(GraphPath<Station, DefaultWeightedEdge> path, List<Section> sections, List<Line> lines) {
		List<Station> stationsInPath = fetchStationsInPath(path);

		List<Section> foundSections = IntStream.range(0, stationsInPath.size() - 1)
			.mapToObj(i -> findSectionByStations(sections, stationsInPath.get(i), stationsInPath.get(i + 1)))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		return lines.stream()
			.filter(line -> line.isContainsAnySections(foundSections))
			.collect(Collectors.toList());
	}


	private Section findSectionByStations(List<Section> sections, Station upStation, Station downStation) {
		return sections.stream()
			.filter(section -> section.isSameUpStation(upStation) && section.isSameDownStation(downStation))
			.findFirst()
			.orElse(null);
	}

}
