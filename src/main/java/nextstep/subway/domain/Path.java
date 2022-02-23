package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public Sections getSections() { return sections; }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() { return sections.totalDuration();}

    public int extractAdditionalFee(List<Line> lines) {
        List<Section> sections = this.sections.getSections();
        return lines.stream()
                .filter(line -> line.getSections().stream().anyMatch(sections::contains))
                .mapToInt(Line::getAdditionalFee)
                .sum();
    }
}
