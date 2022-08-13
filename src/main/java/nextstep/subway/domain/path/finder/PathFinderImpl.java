package nextstep.subway.domain.path.finder;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.common.exception.CustomException;
import nextstep.common.exception.PathErrorMessage;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathType;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public abstract class PathFinderImpl implements PathFinder {

  final List<Line> lines;
  private final PathType pathType;

  PathFinderImpl(List<Line> lines, PathType pathType) {
    this.lines = lines;
    this.pathType = pathType;
  }

  @Override
  public Path findPath(Station source, Station target) {
    validateStationEquals(source, target);

    SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

    addStationGraph(graph);
    addStationEdge(graph);
    addStationEdgeOpposite(graph);

    List<Section> sections = getShortestPath(source, target, graph);

    return new Path(new Sections(sections));
  }

  private void validateStationEquals(Station source, Station target) {
    if (source.equals(target)) {
      throw new CustomException(PathErrorMessage.PATH_DUPLICATION);
    }
  }

  // 지하철 역(정점)을 등록
  private void addStationGraph(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
    lines.stream()
        .flatMap(it -> it.getStations().stream())
        .distinct()
        .collect(Collectors.toList())
        .forEach(graph::addVertex);
  }

  private List<Section> getShortestPath(Station source, Station target, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
    DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

    return result.getEdgeList().stream()
        .map(SectionEdge::getSection)
        .collect(Collectors.toList());
  }

  private void addStationEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
    lines.stream()
        .flatMap(it -> it.getSections().stream())
        .forEach(it -> {
          SectionEdge sectionEdge = SectionEdge.of(it);
          graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
          graph.setEdgeWeight(sectionEdge, getWeight(it));
        });
  }

  private void addStationEdgeOpposite(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
    lines.stream()
        .flatMap(it -> it.getSections().stream())
        .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
        .forEach(it -> {
          SectionEdge sectionEdge = SectionEdge.of(it);
          graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
          graph.setEdgeWeight(sectionEdge, getWeight(it));
        });
  }

  private int getWeight(Section section) {
    if (pathType == PathType.DISTANCE) {
      return section.getDistance();
    }

    return section.getDuration();
  }
}
