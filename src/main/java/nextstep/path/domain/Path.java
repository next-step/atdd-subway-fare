package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private final List<Station> stations;
    private final List<Line> usedLine;
    private final int distance;
    private final int duration;

    public Path(final List<Station> stations, final List<PathSection> pathSections) {
        this.stations = stations;
        this.usedLine = pathSections.stream().map(PathSection::getLine).distinct().collect(Collectors.toList());
        this.distance = pathSections.stream().mapToInt(PathSection::getDistance).sum();
        this.duration = pathSections.stream().mapToInt(PathSection::getDuration).sum();
    }


    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public List<Line> getUsedLine() {
        return usedLine;
    }
}
