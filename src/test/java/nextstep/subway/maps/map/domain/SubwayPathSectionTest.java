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
        // 5:30 첫차에 10분 간격, 역간 소요시간은 전부 2분
        subwayPathSection = new SubwayPathSection(fixture.line3);
    }

    @DisplayName("승차시간을 계산한다(정방향)")
    @Test
    void getRideTimeTest() {
        // given
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation6, fixture.line3));
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation7, fixture.line3));
        LocalTime time = LocalTime.of(7, 35);
        LocalTime expected = LocalTime.of(7, 40);

        // when
        LocalTime departureTime = subwayPathSection.getRideTime(time);

        // then
        assertThat(departureTime).isEqualTo(expected);
    }

    @DisplayName("하차시간을 계산한다(정방향)")
    @Test
    void getAlightTimeTest() {
        // given
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation6, fixture.line3));
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation7, fixture.line3));
        LocalTime time = LocalTime.of(7, 35);
        LocalTime expected = LocalTime.of(7, 44);

        // when
        LocalTime departureTime = subwayPathSection.getAlightTime(time);

        // then
        assertThat(departureTime).isEqualTo(expected);
    }

    @DisplayName("승차시간을 계산한다(역방향)")
    @Test
    void getReverseRideTimeTest() {
        // given
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation6, fixture.line3));
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation5, fixture.line3));
        LocalTime time = LocalTime.of(7, 35);
        LocalTime expected = LocalTime.of(7, 44);

        // when
        LocalTime departureTime = subwayPathSection.getRideTime(time);

        // then
        assertThat(departureTime).isEqualTo(expected);
    }

    @DisplayName("하차시간을 계산한다(역방향)")
    @Test
    void getReverseAlightTimeTest() {
        // given
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation6, fixture.line3));
        subwayPathSection.addLineStationEdge(new LineStationEdge(fixture.lineStation5, fixture.line3));
        LocalTime time = LocalTime.of(7, 35);
        LocalTime expected = LocalTime.of(7, 48);

        // when
        LocalTime departureTime = subwayPathSection.getAlightTime(time);

        // then
        assertThat(departureTime).isEqualTo(expected);
    }
}