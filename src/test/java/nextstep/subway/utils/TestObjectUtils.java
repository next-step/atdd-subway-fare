package nextstep.subway.utils;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.station.domain.Station;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;

public class TestObjectUtils {
    public static Station createStation(Long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }

    public static Line createLine(Long id, String name, String color) {
        return createLine(id, name, color, 0);
    }

    public static Line createLine(Long id, String name, String color, Integer extraFare) {
        Line line1 = new Line(name, color, LocalTime.of(05, 30), LocalTime.of(23, 30), 10, extraFare);
        ReflectionTestUtils.setField(line1, "id", id);
        return line1;
    }
}
