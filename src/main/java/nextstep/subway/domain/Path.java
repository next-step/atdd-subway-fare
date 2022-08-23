package nextstep.subway.domain;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Path {
    private Sections sections;

    public Sections getSections() {
        return sections;
    }

    public List<Line> findIncludedLines() {
        List<Section> extractSections = this.sections.getSections();
        return extractSections.stream()
                              .map(Section::getLine)
                              .collect(Collectors.toList());
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
