package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 30, 5);
        이호선.addSection(교대역, 강남역, 30, 10);
        삼호선.addSection(교대역, 남부터미널역, 50, 1);
        삼호선.addSection(남부터미널역, 양재역, 50, 2);
    }

    @Test
    void findPathByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
    }

    @Test
    void findPathOppositelyByDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
    }

    @Test
    void findPathByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DURATION);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
    }

    @Test
    void findPathOppositelyByDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, PathType.DURATION);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 남부터미널역, 교대역));
    }

    @DisplayName("요금 계산 = 기본요금")
    @Test
    void calculateFare1() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 5, 5);
        이호선.addSection(교대역, 강남역, 5, 10);
        삼호선.addSection(교대역, 남부터미널역, 50, 1);
        삼호선.addSection(남부터미널역, 양재역, 50, 2);

        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.calculateFare(0)).isEqualTo(1250);
    }

    @DisplayName("요금 계산 = 기본요금 + 10 ~ 50km 요금")
    @Test
    void calculateFare2() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 20, 5);
        이호선.addSection(교대역, 강남역, 30, 10);
        삼호선.addSection(교대역, 남부터미널역, 50, 1);
        삼호선.addSection(남부터미널역, 양재역, 50, 2);

        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.calculateFare(0)).isEqualTo(1250 + 800); //총거리 50 = 기본10 + 추가40 = 1250 + 800
    }

    @DisplayName("요금 계산 = 기본요금 + 10 ~ 50km 요금 + 50km 초과요금")
    @Test
    void calculateFare3() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 8, 5);
        이호선.addSection(교대역, 강남역, 50, 10);
        삼호선.addSection(교대역, 남부터미널역, 50, 1);
        삼호선.addSection(남부터미널역, 양재역, 50, 2);

        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.calculateFare(0)).isEqualTo(1250 + 800 + 100); //총거리 58 = 기본10 + 추가40 + 추가8= 1250 + 1000 + 100
    }

    @DisplayName("요금 계산 = 기본요금 + 노선요금")
    @Test
    void calculateFare4() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red", 500);
        이호선 = new Line("2호선", "red", 400);
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 5, 5);
        이호선.addSection(교대역, 강남역, 5, 10);
        삼호선.addSection(교대역, 남부터미널역, 50, 1);
        삼호선.addSection(남부터미널역, 양재역, 50, 2);

        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.calculateFare(22)).isEqualTo(1250 + 500);
    }

    @DisplayName("요금 계산 = 기본요금 + 청소년요금")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void calculateFare5(int age) {
        // given
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 5, 5);
        이호선.addSection(교대역, 강남역, 5, 10);
        삼호선.addSection(교대역, 남부터미널역, 50, 1);
        삼호선.addSection(남부터미널역, 양재역, 50, 2);

        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);
        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.calculateFare(age)).isEqualTo((int) ((1250 - 350) * 0.8));
    }

    @DisplayName("요금 계산 = 기본요금 + 청소년요금")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void calculateFare6(int age) {
        // given
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 5, 5);
        이호선.addSection(교대역, 강남역, 5, 10);
        삼호선.addSection(교대역, 남부터미널역, 50, 1);
        삼호선.addSection(남부터미널역, 양재역, 50, 2);

        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);
        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(path.calculateFare(age)).isEqualTo((int) ((1250 - 350) * 0.5));
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
