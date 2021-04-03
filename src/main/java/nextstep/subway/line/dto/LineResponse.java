package nextstep.subway.line.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

public class LineResponse {

  private Long id;
  private String name;
  private String color;
  private List<StationResponse> stations;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;
  private int additionalFare;

  public LineResponse() {
  }

  public LineResponse(
      Long id, String name, String color, List<StationResponse> stations,
      LocalDateTime createdDate, LocalDateTime modifiedDate, int additionalFare
  ) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.stations = stations;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
    this.additionalFare = additionalFare;
  }

  public static LineResponse of(Line line) {
    List<StationResponse> stations = line.getStations().stream()
        .map(it -> StationResponse.of(it))
        .collect(Collectors.toList());
    return new LineResponse(line.getId(), line.getName(), line.getColor(), stations,
        line.getCreatedDate(), line.getModifiedDate(), line.getAdditionalFare());
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public List<StationResponse> getStations() {
    return stations;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public int getAdditionalFare() {
    return additionalFare;
  }

}
