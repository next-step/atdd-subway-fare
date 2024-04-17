package nextstep.path;

import nextstep.line.Line;
import nextstep.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class Path {

    private List<Station> stations;
    private List<PathSection> pathSections;

    public Path(List<Station> stations, List<PathSection> pathSections) {
        this.stations = stations;
        this.pathSections = pathSections;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return pathSections.stream().mapToInt(PathSection::getDistance).sum();
    }

    public int getDuration() {
        return pathSections.stream().mapToInt(PathSection::getDuration).sum();
    }

    public List<Line> getUsedLine() {
        return pathSections.stream().map(PathSection::getLine).distinct().collect(Collectors.toList());
    }
}
