package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class PathResult {

  private Sections sections;
  private Stations stations;

  public PathResult(Stations stations, Sections sections) {
    this.stations = stations;
    this.sections = sections;
  }

  public List<Station> getStations() {
    return stations.getStations();
  }

  public int getTotalDistance() {
    return sections.getTotalDistance();
  }

  public int getTotalDuration() {
    return sections.getTotalDuration();
  }

  public int getMaxAdditionalFare() {
    return sections.getSections()
        .stream()
        .mapToInt(section -> section.getLine().getAdditionalFare())
        .max()
        .orElse(0);
  }


}
