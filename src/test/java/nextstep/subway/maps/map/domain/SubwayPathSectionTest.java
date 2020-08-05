package nextstep.subway.maps.map.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathSectionTest {

    private SubwayPathTestFixture fixture;
    private SubwayPathSection subwayPathSection;

    @BeforeEach
    void setUp() {

        fixture = new SubwayPathTestFixture();
        // given
        // 5:30 첫차에 10분 간격
        subwayPathSection = new SubwayPathSection(fixture.line3);
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation5, fixture.line3));
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation6, fixture.line3));
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation7, fixture.line3));
    }

    @DisplayName("승차시간을 계산한다")
    @Test
    void getRideTimeTest() {
        // given
        LocalTime time = LocalTime.of(7, 35);
        LocalTime expected = LocalTime.of(7, 40);

        // when
        LocalTime departureTime = subwayPathSection.getRideTime(time);

        // then
        assertThat(departureTime).isEqualTo(expected);
    }

    @DisplayName("하차시간을 계산한다")
    @Test
    void getAlightTimeTest() {
        // given
        LocalTime time = LocalTime.of(7, 35);
        LocalTime expected = LocalTime.of(7, 42);

        // when
        LocalTime departureTime = subwayPathSection.getAlightTime(time);

        // then
        assertThat(departureTime).isEqualTo(expected);
    }
}