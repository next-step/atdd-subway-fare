package nextstep.subway.domain;

import static nextstep.subway.utils.StringDateTimeConverter.convertStringToDateTime;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AllKShortestPathsTest {
    private static final String START_TIME = "202202200600";

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 삼성역;
    private Station 대치역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        createSubwayMap();
    }

    @DisplayName("단일 구간에서 상행선 방향의 열차 탑승 시간을 확인한다.")
    @Test
    void findTrainTime_singleSection_upDirection_Test() {
        // given
        PathsFinder pathsFinder = new PathsFinder(convertStringToDateTime(START_TIME));

        // when
        LocalDateTime trainTime = pathsFinder.findTrainTime(
            new Section(신분당선, 양재역, 강남역, 9, 2), convertStringToDateTime("202202200605"));

        // then
        assertThat(trainTime).isEqualTo(convertStringToDateTime("202202200620"));
    }

    @DisplayName("단일 구간에서 하행선 방향의 열차 탑승 시간을 확인한다.")
    @Test
    void findTrainTime_singleSection_downDirection_Test() {
        // given
        PathsFinder pathsFinder = new PathsFinder(convertStringToDateTime(START_TIME));

        // when
        LocalDateTime trainTime = pathsFinder.findTrainTime(
            new Section(신분당선, 강남역, 양재역, 9, 2), convertStringToDateTime("202202200600"));

        // then
        assertThat(trainTime).isEqualTo(convertStringToDateTime("202202200600"));
    }

    @DisplayName("전체 노선에서 특정 구간의 상행선 열차 탑승 시간을 확인한다.")
    @Test
    void findTrainTime_UpDirection_Test() {
        // given
        PathsFinder pathsFinder = new PathsFinder(convertStringToDateTime(START_TIME));

        // when
        LocalDateTime trainTime = pathsFinder.findTrainTime(
            new Section(이호선, 강남역, 교대역, 8, 3), convertStringToDateTime(START_TIME));

        assertThat(trainTime).isEqualTo(LocalDateTime.of(2022, 02, 20, 06, 03));
    }

    @DisplayName("전체 노선에서 특정 구간의 하행선 열차 탑승 시간을 확인한다.")
    @Test
    void findTrainTime_DownDirection_Test() {
        // given
        PathsFinder pathsFinder = new PathsFinder(convertStringToDateTime(START_TIME));

        // when
        LocalDateTime trainTime = pathsFinder.findTrainTime(
            new Section(이호선, 교대역, 강남역, 8, 3), convertStringToDateTime(START_TIME));

        assertThat(trainTime).isEqualTo(LocalDateTime.of(2022, 02, 20, 06, 00));
    }

    @DisplayName("경로의 구간이 어느 방향행인지 확인해보니 상행이다.")
    @Test
    void findPathUpDirectionTest() {
        // given
        PathsFinder pathsFinder = new PathsFinder(convertStringToDateTime(START_TIME));

        // when
        PathDirection direction = new Section(이호선, 강남역, 교대역, 8, 3).findPathDirection();

        // then
        assertThat(direction).isEqualTo(PathDirection.UP);
    }

    @DisplayName("경로의 구간이 어느 방향행인지 확인해보니 하행이다.")
    @Test
    void findPathDownDirectionTest() {
        // given
        PathsFinder pathsFinder = new PathsFinder(convertStringToDateTime(START_TIME));

        // when
        PathDirection direction = new Section(삼호선, 남부터미널역, 양재역, 8, 3).findPathDirection();

        // then
        assertThat(direction).isEqualTo(PathDirection.DOWN);
    }

    private void createSubwayMap() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        삼성역 = createStation(5L, "삼성역");
        대치역 = createStation(6L, "대치역");

        신분당선 = Line.of("신분당선", "red", 2000
            , "0520", "2340", 20);
        이호선 = Line.of("2호선", "red", 900
            , "0500", "2330", 10);
        삼호선 = Line.of("3호선", "red", 1100
            , "0530", "2300", 5);

        신분당선.addSection(강남역, 양재역, 9, 2);
        이호선.addSection(교대역, 강남역, 8, 3);
        이호선.addSection(강남역, 삼성역, 8, 3);
        삼호선.addSection(교대역, 남부터미널역, 6, 5);
        삼호선.addSection(남부터미널역, 양재역, 6, 5);
        삼호선.addSection(양재역, 대치역, 2, 5);
    }

    private Station createStation(long id, String name) {
        Station station = Station.of(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

}