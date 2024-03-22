package nextstep.subway.domain.path;

import nextstep.subway.domain.entity.Section;

import java.util.List;

public class Path {
    private final List<Long> stations;
    private final List<Section> sections;

    public Path(List<Long> vertexs, List<Section> sections) {
        this.stations = vertexs;
        this.sections = sections;
    }

    public List<Long> getStations() {
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }

    public int getDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public int getDuration() {
        return sections.stream()
                .mapToInt(Section::getDuration)
                .sum();
    }
}
