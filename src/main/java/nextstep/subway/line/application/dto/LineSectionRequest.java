package nextstep.subway.line.application.dto;

import lombok.Getter;

@Getter
public class LineSectionRequest {
  private final Long upStationId;
  private final Long downStationId;
  private final Integer distance;

  public LineSectionRequest(Long upStationId, Long downStationId, Integer distance) {
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }
}
