package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import nextstep.subway.station.domain.Station;

public class Path {
  private final List<Station> stations;
  @Getter private final long totalDistance;
  @Getter private final long totalDuration;

  private Path(List<Station> stations, long totalDistance, long totalDuration) {
    this.stations = stations;
    this.totalDistance = totalDistance;
    this.totalDuration = totalDuration;
  }

  public static Path empty() {
    return new Path(Collections.emptyList(), 0, 0);
  }

  public static Path of(List<Station> stations, long totalDistance, long totalDuration) {
    return new Path(stations, totalDistance, totalDuration);
  }

  public List<Station> getStations() {
    return Collections.unmodifiableList(stations);
  }
}
