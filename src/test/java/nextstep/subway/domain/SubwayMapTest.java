package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.domain.exception.PathNotFoundException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 지도 관련 기능")
class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 죽전역;
    private Station 정자역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private Line 수인분당선;
    private List<Line> lines = new ArrayList<>();

    /**
     * 교대역    --- *2호선* 10km   ---   강남역
     * |                                 |
     * *3호선* 900km                 *신분당선* 10km
     * |                                 |
     * 남부터미널역  --- *3호선* 10km ---   양재             정자역 --- *수인분당선* 20km --- 죽전역
     */
    @BeforeEach
    public void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        죽전역 = new Station("죽전역");
        정자역 = new Station("정자역");

        이호선 = new Line("2호선", "red");
        신분당선 = new Line("신분당선", "orange");
        삼호선 = new Line("3호선", "green");
        수인분당선 = new Line("수인분당선", "yellow");

        이호선.addSection(교대역, 강남역, 10);
        삼호선.addSection(교대역, 남부터미널역, 900);
        삼호선.addSection(남부터미널역, 양재역, 10);
        신분당선.addSection(강남역, 양재역, 10);
        수인분당선.addSection(죽전역, 정자역, 20);

        lines.addAll(List.of(이호선, 삼호선, 신분당선));
    }

    @DisplayName("거리 기준으로 경로를 조회한다.")
    @Test
    void findByPath() {
        SubwayMap subwayMap = new SubwayMap(this.lines);

        Path path = subwayMap.findPath(교대역, 양재역);

        assertAll(
                () -> assertThat(path.getStations()).containsExactly(교대역, 강남역, 양재역),
                () -> assertThat(path.extractDistance()).isEqualTo(20)
        );
    }

    @DisplayName("존재하지 않는 역으로 경로를 조회한다.")
    @Test
    void findByPathNotExist() {
        SubwayMap subwayMap = new SubwayMap(this.lines);
        Station 존재하지_않는_역1 = new Station("존재하지 않는 역1");
        Station 존재하지_않는_역2 = new Station("존재하지 않는 역2");

        assertAll(
                () -> assertThatThrownBy(() -> subwayMap.findPath(강남역, 존재하지_않는_역1))
                        .isInstanceOf(PathNotFoundException.class),
                () -> assertThatThrownBy(() -> subwayMap.findPath(존재하지_않는_역1, 강남역))
                        .isInstanceOf(PathNotFoundException.class),
                () -> assertThatThrownBy(() -> subwayMap.findPath(존재하지_않는_역1, 존재하지_않는_역2))
                        .isInstanceOf(PathNotFoundException.class)
        );
    }

    @DisplayName("연결되지 않는 역으로 경로를 조회한다.")
    @Test
    void findByPathNotConnected() {
        SubwayMap subwayMap = new SubwayMap(this.lines);

        assertAll(
                () -> assertThatThrownBy(() -> subwayMap.findPath(강남역, 죽전역))
                        .isInstanceOf(PathNotFoundException.class),
                () -> assertThatThrownBy(() -> subwayMap.findPath(죽전역, 강남역))
                        .isInstanceOf(PathNotFoundException.class)
        );
    }

    @DisplayName("경로를 조회한다.")
    @Test
    void findPath() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @DisplayName("반대로 경로를 조회한다.")
    @Test
    void findPathOppositely() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    @DisplayName("경로 타입 거리를 기준으로 경로를 조회한다.")
    @Test
    void findPathTypeDistance() {
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(양재역, 교대역, PathType.DISTANCE);

        assertAll(
                () -> assertThat(path.extractDistance()).isEqualTo(20),
                () -> assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역))
        );
    }
}
