package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResult {
    private Sections sections;

    public PathResult(Sections sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getTotalDistance() {
        return sections.getTotalDistance();
    }

    public int getTotalDuration() {
        return sections.getTotalDuration();
    }

    public int getAdditionalFareOf(List<Line> allLine) {
        List<Station> pathStations = sections.getStations();
        return allLine.stream()
                      .filter(line ->
                          line.getStations()
                              .stream()
                              .anyMatch(pathStations::contains)
                      )
                      .map(Line::getAdditionalFare)
                      .reduce(0, Math::max);
    }
}
