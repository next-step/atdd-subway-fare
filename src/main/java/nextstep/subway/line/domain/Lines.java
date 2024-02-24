package nextstep.subway.line.domain;

import nextstep.subway.line.section.domain.Section;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.function.Consumer;

public class Lines {

    private final List<Line> lineList;

    private Lines(List<Line> lineList) {
        this.lineList = lineList;
    }

    public static Lines from(List<Line> lineList) {
        return new Lines(List.copyOf(lineList));
    }

    public boolean existStation(Station station) {
        return this.lineList.stream().anyMatch(line -> line.existStation(station));
    }

    public void forEach(Consumer<Line> action) {
        lineList.forEach(action);
    }

    public Long calculateValue(List<Station> shortestPath,
                               PathType type) {
        Long totalDistance = 0L;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Station source = shortestPath.get(i);
            Station target = shortestPath.get(i + 1);
            for (Line line : this.lineList) {
                totalDistance += line.calculateValue(source, target, type);
            }
        }
        return totalDistance;

    }
}
