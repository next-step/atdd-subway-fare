package nextstep.subway.line.application.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.line.domain.Line;

@Getter
public class LineRequest {
  private final String name;
  private final String color;
  private final Long upStationId;
  private final Long downStationId;
  private final Integer distance;
  private final Integer duration;

  @Builder
  public LineRequest(
      String name,
      String color,
      Long upStationId,
      Long downStationId,
      Integer distance,
      Integer duration) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
    this.duration = duration;
  }

  public Line toLine() {
    return new Line(name, color);
  }

  public LineSectionRequest toLineSection() {
    return new LineSectionRequest(upStationId, downStationId, distance, duration);
  }
}
