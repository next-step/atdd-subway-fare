package nextstep.subway.unit.utils;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

public final class ClassicUnitTestUtils {

    public static Line getLine(String name, String color, Long distance, Integer duration, Station upStation, Station downStation, Long id) {
        Line secondaryLine = spy(Line.builder()
                .name(name)
                .color(color)
                .distance(distance)
                .upStation(upStation)
                .downStation(downStation)
                .duration(duration)
                .build());
        given(upStation.getId()).willReturn(id);
        return secondaryLine;
    }

    public static Station getStation(String name, Long id) {
        Station station = spy(Station.create(() -> name));
        given(station.getId()).willReturn(id);
        return station;
    }
}
