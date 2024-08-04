package nextstep.subway.path.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.domain.SubwayGraph;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GraphService {
  private final LineService lineService;

  public SubwayGraph loadGraph(PathType type) {
    SubwayGraph graph = new SubwayGraph(type);
    List<Line> lines = lineService.findAllLines();
    lines.forEach(graph::addLine);
    return graph;
  }
}
