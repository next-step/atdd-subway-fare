package nextstep.subway.maps.map.domain;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    private SubwayPath subwayPath;

    @BeforeEach
    void setUp() {
        SubwayPathTestFixture fixture = new SubwayPathTestFixture();

        List<LineStationEdge> lineStations = Lists.newArrayList(
                new LineStationEdge(fixture.lineStation6, fixture.line3),
                new LineStationEdge(fixture.lineStation7, fixture.line3)
        );
        subwayPath = new SubwayPath(lineStations);
    }

    @DisplayName("출발역을 찾는 메소드 테스트")
    @Test
    void getSourceTest() {
        // when
        Long source = subwayPath.getSourceStationId();

        // then
        assertThat(source).isEqualTo(1L);
    }

    @DisplayName("도착역을 찾는 메소드 테스트")
    @Test
    void getTargetTest() {
        // when
        Long source = subwayPath.getTargetStationId();

        // then
        assertThat(source).isEqualTo(3L);
    }
}