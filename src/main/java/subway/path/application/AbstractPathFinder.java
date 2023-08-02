package subway.path.application;

import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;

public abstract class AbstractPathFinder {

    protected Long getTotalDurationInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDuration)
                .reduce(0L, Long::sum);
    }

    protected Long getTotalDistanceInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }
}
