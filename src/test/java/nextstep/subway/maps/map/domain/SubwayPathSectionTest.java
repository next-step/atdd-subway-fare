package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathSectionTest {

    private SubwayPathTestFixture fixture;

    @BeforeEach
    void setUp() {
        fixture = new SubwayPathTestFixture();
    }

    @DisplayName("승차시간을 계산한다(정방향)")
    @Test
    void getRideTimeTest() {
        // given
        // 5:30 첫차에 10분 간격, 역간 소요시간은 전부 2분
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
                new LineStationEdge(fixture.lineStation6, fixture.line3),
                new LineStationEdge(fixture.lineStation7, fixture.line3)
        );

        PathDirection direction = PathDirection.getDirection(lineStationEdges);

        SubwayPathSection subwayPathSection = getSubwayPathSection(lineStationEdges, direction);

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
        // 5:30 첫차에 10분 간격, 역간 소요시간은 전부 2분
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
                new LineStationEdge(fixture.lineStation6, fixture.line3),
                new LineStationEdge(fixture.lineStation7, fixture.line3)
        );

        PathDirection direction = PathDirection.getDirection(lineStationEdges);

        SubwayPathSection subwayPathSection = getSubwayPathSection(lineStationEdges, direction);

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
        // 5:30 첫차에 10분 간격, 역간 소요시간은 전부 2분
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
                new LineStationEdge(fixture.lineStation6, fixture.line3),
                new LineStationEdge(fixture.lineStation5, fixture.line3)
        );

        PathDirection direction = PathDirection.getDirection(lineStationEdges);

        SubwayPathSection subwayPathSection = getSubwayPathSection(lineStationEdges, direction);

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
        // 5:30 첫차에 10분 간격, 역간 소요시간은 전부 2분
        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
                new LineStationEdge(fixture.lineStation6, fixture.line3),
                new LineStationEdge(fixture.lineStation5, fixture.line3)
        );

        PathDirection direction = PathDirection.getDirection(lineStationEdges);

        SubwayPathSection subwayPathSection = getSubwayPathSection(lineStationEdges, direction);

        LocalTime time = LocalTime.of(7, 35);
        LocalTime expected = LocalTime.of(7, 48);

        // when
        LocalTime departureTime = subwayPathSection.getAlightTime(time);

        // then
        assertThat(departureTime).isEqualTo(expected);
    }

    private SubwayPathSection getSubwayPathSection(List<LineStationEdge> lineStationEdges, PathDirection direction) {
        return direction.createSection(fixture.line3, lineStationEdges);
    }
}