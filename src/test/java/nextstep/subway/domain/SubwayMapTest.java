package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {
    @DisplayName("최단 거리 기준으로 경로를 조회한다")
    @Test
    void findPathByDistanceByDistance() {
        // given
        Station 교대역 = new Station(1L, "교대역");
        Station 강남역 = new Station(2L, "강남역");
        Station 양재역 = new Station(3L, "양재역");
        Station 남부터미널역 = new Station(4L, "남부터미널역");
        Line 이호선 = new Line("2호선", "green");
        이호선.addSection(교대역, 강남역, 10, 1);
        Line 신분당선 = new Line("신분당선", "red");
        신분당선.addSection(강남역, 양재역, 10, 3);
        Line 삼호선 = new Line("3호선", "orange");
        삼호선.addSection(교대역, 남부터미널역, 2, 2);
        삼호선.addSection(남부터미널역, 양재역, 3, 3);
        List<Line> lines = Arrays.asList(이호선, 신분당선, 삼호선);

        // when
        Path path = new SubwayDistanceMap(lines).findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.extractDistance()).isEqualTo(5);
        assertThat(path.extractDuration()).isEqualTo(5);
    }

    @DisplayName("최소 시간 기준으로 경로를 조회한다.")
    @Test
    void findPathByDistanceByDuration() {
        // given
        Station 교대역 = new Station(1L, "교대역");
        Station 강남역 = new Station(2L, "강남역");
        Station 양재역 = new Station(3L, "양재역");
        Station 남부터미널역 = new Station(4L, "남부터미널역");
        Line 이호선 = new Line("2호선", "green");
        이호선.addSection(교대역, 강남역, 10, 1);
        Line 신분당선 = new Line("신분당선", "red");
        신분당선.addSection(강남역, 양재역, 10, 3);
        Line 삼호선 = new Line("3호선", "orange");
        삼호선.addSection(교대역, 남부터미널역, 2, 2);
        삼호선.addSection(남부터미널역, 양재역, 3, 3);
        List<Line> lines = Arrays.asList(이호선, 신분당선, 삼호선);

        // when
        Path path = new SubwayDurationMap(lines).findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 강남역, 양재역);
        assertThat(path.extractDistance()).isEqualTo(20);
        assertThat(path.extractDuration()).isEqualTo(4);
    }
}
