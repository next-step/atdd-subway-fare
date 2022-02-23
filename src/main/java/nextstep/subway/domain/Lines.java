package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Station> getStations() {
        Set<Station> stations = new HashSet<>();
        for (Line line : lines) {
            stations.addAll(line.getStations());
        }
        return stations;
    }

    public List<Section> getSections() {
        List<Section> sections = new ArrayList<>();
        for (Line line : lines) {
            sections.addAll(line.getSections());
        }
        return sections;
    }
}
