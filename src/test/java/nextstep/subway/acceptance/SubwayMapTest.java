package nextstep.subway.acceptance;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;


    /**
     * (d=distance, t=duration)
     * 교대역    --- *2호선* ---        강남역
     * |              (d10,t3)          |
     * *3호선* (d2,t2)        (d10, t5) *신분당선*
     * |                 (d3,t10)       |
     * 남부터미널역  --- *3호선* ---      양재
     */
    @BeforeEach
    void setUp() {
        신분당선 = new Line();
        이호선 = new Line();
        삼호선 = new Line();
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        이호선.addSection(교대역, 강남역, 10, 3);
        신분당선.addSection(강남역, 양재역, 10, 5);
        삼호선.addSection(교대역, 남부터미널역, 2, 2);
        삼호선.addSection(남부터미널역, 양재역, 3, 10);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPath_distance() {
        SubwayMap subwayMap = new SubwayMap(Arrays.asList(신분당선, 이호선, 삼호선));
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);

        assertThat(path.getStations().stream().map(Station::getName))
                .containsExactly("교대역", "남부터미널역", "양재역");
        assertThat(path.extractDistance()).isEqualTo(5);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPath_duration() {
        SubwayMap subwayMap = new SubwayMap(Arrays.asList(신분당선, 이호선, 삼호선));
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DURATION);

        assertThat(path.getStations().stream().map(Station::getName))
                .containsExactly("교대역", "강남역", "양재역");
        assertThat(path.extractDuration()).isEqualTo(8);
    }
}
