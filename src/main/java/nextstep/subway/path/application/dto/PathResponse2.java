package nextstep.subway.path.application.dto;

import java.util.List;
import lombok.Getter;
import nextstep.subway.path.domain.Path2;
import nextstep.subway.station.application.dto.StationResponse;

@Getter
public class PathResponse2 {
  private final List<StationResponse> stations;
  private final long distance;
  private final long duration;

  public PathResponse2(List<StationResponse> stations, long distance, long duration) {
    this.stations = stations;
    this.distance = distance;
    this.duration = duration;
  }

  public static PathResponse2 from(Path2 path) {
    return new PathResponse2(
        StationResponse.listOf(path.getStations()),
        path.getTotalDistance(),
        path.getTotalDuration());
  }
}
