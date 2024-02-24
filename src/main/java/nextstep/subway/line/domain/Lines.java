package nextstep.subway.line.domain;

import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

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
        return IntStream.range(0, shortestPath.size() - 1)
                .mapToLong(i -> calculateValue(shortestPath, type, i))
                .sum();
    }

    private long calculateValue(List<Station> shortestPath,
                                PathType type,
                                int index) {
        Station source = shortestPath.get(index);
        Station target = shortestPath.get(index + 1);
        return this.lineList.stream()
                .mapToLong(line -> line.calculateValue(source, target, type))
                .sum();
    }
}
