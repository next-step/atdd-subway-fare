package subway.path.domain;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Builder
public class Path {
    private long totalFare;

    private List<Section> sections;

    public long getTotalDistance() {
        return this.sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }

    public long getTotalDuration() {
        return this.sections.stream()
                .map(Section::getDuration)
                .reduce(0L, Long::sum);
    }

    public List<Station> getStations() {
        return this.sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }
}
