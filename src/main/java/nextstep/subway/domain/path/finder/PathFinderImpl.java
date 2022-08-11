package nextstep.subway.domain.path.finder;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public abstract class PathFinderImpl implements PathFinder {

  final List<Line> lines;

  PathFinderImpl(List<Line> lines) {
    this.lines = lines;
  }

  @Override
  public Path findPath(Station source, Station target) {
    SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

    addStationGraph(graph);
    addStationEdge(graph);
    addStationEdgeOpposite(graph);

    // 다익스트라 최단 경로 찾기
    DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

    List<Section> sections = result.getEdgeList().stream()
        .map(SectionEdge::getSection)
        .collect(Collectors.toList());

    return new Path(new Sections(sections));
  }

  // 지하철 역(정점)을 등록
  private void addStationGraph(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
    lines.stream()
        .flatMap(it -> it.getStations().stream())
        .distinct()
        .collect(Collectors.toList())
        .forEach(graph::addVertex);
  }
  abstract void addStationEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph);
  abstract void addStationEdgeOpposite(SimpleDirectedWeightedGraph<Station, SectionEdge> graph);
}
