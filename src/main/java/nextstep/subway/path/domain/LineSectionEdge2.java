package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.line.domain.LineSection;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
public class LineSectionEdge2 extends DefaultWeightedEdge {
  private final LineSection lineSection;

  public LineSectionEdge2(LineSection lineSection) {
    this.lineSection = lineSection;
  }

  public static LineSectionEdge2 of(LineSection lineSection) {
    return new LineSectionEdge2(lineSection);
  }
}
