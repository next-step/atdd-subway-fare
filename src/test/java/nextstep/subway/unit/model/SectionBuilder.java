package nextstep.subway.unit.model;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

public final class SectionBuilder {

    public static Section createSection(Line line, Station upStation, Station downStation, int distance, int duration) {
        return new Section.Builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build();
    }
}
