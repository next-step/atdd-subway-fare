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

  private SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

  final List<Line> lines;

  PathFinderImpl(List<Line> lines) {
    this.lines = lines;
  }

  @Override
  public Path findPath(Station source, Station target, PathType pathType) {
    validateStationEquals(source, target);

    addStationGraph();

    Sections sections = getShortestPath(source, target, pathType);
    int shortDistance = getShortDistance(source, target, pathType, sections);

    return new Path(sections, shortDistance);
  }

  private void validateStationEquals(Station source, Station target) {
    if (source.equals(target)) {
      throw new CustomException(PathErrorMessage.PATH_DUPLICATION);
    }
  }

  // 지하철 역(정점)을 등록
  private void addStationGraph() {
    lines.stream()
        .flatMap(it -> it.getStations().stream())
        .distinct()
        .collect(Collectors.toList())
        .forEach(graph::addVertex);
  }

  private int getShortDistance(Station source, Station target, PathType pathType, Sections sections) {
    return pathType == PathType.DURATION
        ? getShortestPath(source, target, PathType.DISTANCE).totalDistance()
        : sections.totalDistance();
  }

  private Sections getShortestPath(Station source, Station target, PathType pathType) {
    addStationEdge(pathType);
    addStationEdgeOpposite(pathType);

    DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

    List<Section> sections = result.getEdgeList().stream()
        .map(SectionEdge::getSection)
        .collect(Collectors.toList());

    return new Sections(sections);
  }

  private void addStationEdge(PathType pathType) {
    lines.stream()
        .flatMap(it -> it.getSections().stream())
        .forEach(it -> {
          SectionEdge sectionEdge = SectionEdge.of(it);
          graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
          graph.setEdgeWeight(sectionEdge, it.getWeight(pathType));
        });
  }

  private void addStationEdgeOpposite(PathType pathType) {
    lines.stream()
        .flatMap(it -> it.getSections().stream())
        .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
        .forEach(it -> {
          SectionEdge sectionEdge = SectionEdge.of(it);
          graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
          graph.setEdgeWeight(sectionEdge, it.getWeight(pathType));
        });
  }
}
