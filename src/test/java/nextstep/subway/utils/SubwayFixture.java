package nextstep.subway.utils;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;

public class SubwayFixture {
    public static Station createStation(Long id, String name) {
        return new Station(id, name);
    }

    public static Line createLine(String name, String color, Section section) {
        return new Line(name, color, section);
    }

    public static Section createSection(Station upStation, Station downStation) {
        return new Section(upStation, downStation, (int) (Math.random() * 100 + 1), (int) (Math.random() * 100 + 1));
    }
    public static Section createSection(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, distance, (int) (Math.random() * 100 + 1));
    }
    public static Section createSection(Station upStation, Station downStation, int distance, int transitTime) {
        return new Section(upStation, downStation, distance, transitTime);
    }
}
