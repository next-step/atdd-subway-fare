package nextstep.line;

import nextstep.line.section.Section;
import nextstep.station.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lines {
    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines from(List<Line> lines) {
        return new Lines(List.copyOf(lines));
    }

    public Set<Line> findLinesInPath(List<Station> path) {
        Set<Line> linesInPath = new HashSet<>();

        for (int i = 0; i < path.size() - 1; i++) {
            Station upstation = path.get(i);
            Station downstation = path.get(i + 1);

            for (Line line : lines) {
                for (Section section : line.getSections()) {
                    if (section.isUpstation(upstation) && section.isDownstation(downstation)) {
                        linesInPath.add(line);
                        break;
                    }
                }
            }
        }

        return linesInPath;
    }
}
