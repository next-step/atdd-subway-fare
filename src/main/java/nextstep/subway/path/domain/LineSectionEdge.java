package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.line.domain.LineSection2;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
public class LineSectionEdge extends DefaultWeightedEdge {
  private final LineSection2 lineSection;

  public LineSectionEdge(LineSection2 lineSection) {
    this.lineSection = lineSection;
  }

  public static LineSectionEdge of(LineSection2 lineSection) {
    return new LineSectionEdge(lineSection);
  }

  @Override
  protected double getWeight() {
    return lineSection.getDistance();
  }
}
