package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PathServiceTest {

    @Autowired
    private PathService pathService;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private Station 교대역;
    private Station 강남역;
    private Station 삼성역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 대치역;
    private Station 판교역;
    private Station 광교역;
    private Station 교판역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    /**
     * 교대역    --- *2호선*  ---    강남역  ---  *2호선*  --- 삼성역
     *  |      거리:8, 시간:4        |       거리:8, 시간 3
     *  |                          |
     * *3호선*                   *신분당선*
     거리:5, 시간:5             거리:6, 시간:3
     *  |                          |
     *  |                          |
     * 남부터미널역  --- *3호선* ---  양재역 ---  *3호선*  ---  대치역
     *           거리:5, 시간:5      |      거리: 5, 시간 5
     *                             |
     *                          *신분당선*
     *                        거리:40, 시간 20
     *                             |
     *                             |
     *                           판교역
     *                             |
     *                          *신분당선*
     *                        거리:8, 시간 5
     *                             |
     *                             |
     *                           광교역
     *                             |
     *                             |
     *                          *신분당선*
     *                        거리:1, 시간 1
     *                             |
     *                             |
     *                           교판역
     *
     * 거리10: 교대역~ 양재역
     * 거리11: 강남역~ 남부터미널역
     * 거리15: 교대역~ 대치역
     * 거리16: 교대역~ 삼성역
     * 거리50: 교대역~ 판교역
     * 거리58: 교대역~ 광교역
     * 거리59: 교대역~ 교판역
     */

    @BeforeEach
    void setup() {
        교대역 = stationRepository.save(Station.of("교대역"));
        강남역 = stationRepository.save(Station.of("강남역"));
        삼성역 = stationRepository.save(Station.of("삼성역"));
        남부터미널역 = stationRepository.save(Station.of("남부터미널"));
        양재역 = stationRepository.save(Station.of("양재역"));
        대치역 = stationRepository.save(Station.of("대치역"));
        판교역 = stationRepository.save(Station.of("판교역"));
        광교역 = stationRepository.save(Station.of("광교역"));
        교판역 = stationRepository.save(Station.of("교판역"));

        이호선 = Line.of("이호선", "green");
        이호선.addSection(교대역, 강남역, 8, 4);
        이호선.addSection(강남역, 삼성역, 8, 3);

        신분당선 = Line.of("신분당선", "red");
        신분당선.addSection(강남역, 양재역, 6, 3);
        신분당선.addSection(양재역, 판교역, 40, 20);
        신분당선.addSection(판교역, 광교역, 8, 5);
        신분당선.addSection(광교역, 교판역, 1, 1);

        삼호선 = Line.of("삼호선", "orange");
        삼호선.addSection(교대역, 남부터미널역, 5, 5);
        삼호선.addSection(남부터미널역, 양재역, 5, 5);
        삼호선.addSection(양재역, 대치역, 5, 5);

        lineRepository.save(이호선);
        lineRepository.save(신분당선);
        lineRepository.save(삼호선);
    }

    @DisplayName("10km 거리의 경로조회를 한다.")
    @Test
    void findPathTest_1() {
        PathResponse response = pathService.findPath(교대역.getId(), 양재역.getId(), PathType.DISTANCE);

        assertAll(
            () -> assertThat(response.getStations().stream()
                .mapToLong(value -> value.getId())).containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId()),
            () -> assertThat(response.getDistance()).isEqualTo(10),
            () -> assertThat(response.getDuration()).isEqualTo(10),
            () -> assertThat(response.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("11km 거리의 경로조회 (최소시간 경로와 다른 경로) 를 한다.")
    @Test
    void findPathTest_2() {
        PathResponse response = pathService.findPath(강남역.getId(), 남부터미널역.getId(), PathType.DURATION);

        assertAll(
            () -> assertThat(response.getStations().stream()
                .mapToLong(value -> value.getId())).containsExactly(강남역.getId(), 양재역.getId(), 남부터미널역.getId()),
            () -> assertThat(response.getDistance()).isEqualTo(11),
            () -> assertThat(response.getDuration()).isEqualTo(8),
            () -> assertThat(response.getFare()).isEqualTo(1350)
        );
    }

    @DisplayName("15km 거리의 경로조회 (최소시간 경로와 다른 경로) 를 한다.")
    @Test
    void findPathTest_3() {
        PathResponse response = pathService.findPath(교대역.getId(), 대치역.getId(), PathType.DURATION);

        assertAll(
            () -> assertThat(response.getStations().stream()
                .mapToLong(value -> value.getId())).containsExactly(교대역.getId(), 강남역.getId(), 양재역.getId(), 대치역.getId()),
            () -> assertThat(response.getDistance()).isEqualTo(19),
            () -> assertThat(response.getDuration()).isEqualTo(12),
            () -> assertThat(response.getFare()).isEqualTo(1350)    // 최단거리는 15
        );
    }

    @DisplayName("16km 거리의 경로조회를 한다.")
    @Test
    void findPathTest_4() {
        PathResponse response = pathService.findPath(교대역.getId(), 삼성역.getId(), PathType.DISTANCE);

        assertAll(
            () -> assertThat(response.getStations().stream()
                .mapToLong(value -> value.getId())).containsExactly(교대역.getId(), 강남역.getId(), 삼성역.getId()),
            () -> assertThat(response.getDistance()).isEqualTo(16),
            () -> assertThat(response.getDuration()).isEqualTo(7),
            () -> assertThat(response.getFare()).isEqualTo(1450)
        );
    }

    @DisplayName("50km 거리의 경로조회를 한다.")
    @Test
    void findPathTest_5() {
        PathResponse response = pathService.findPath(교대역.getId(), 판교역.getId(), PathType.DISTANCE);

        assertAll(
            () -> assertThat(response.getStations().stream()
                .mapToLong(value -> value.getId()))
                .containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId(), 판교역.getId()),
            () -> assertThat(response.getDistance()).isEqualTo(50),
            () -> assertThat(response.getDuration()).isEqualTo(30),
            () -> assertThat(response.getFare()).isEqualTo(2050)
        );
    }

    @DisplayName("58km 거리의 경로조회를 한다.")
    @Test
    void findPathTest_6() {
        PathResponse response = pathService.findPath(교대역.getId(), 광교역.getId(), PathType.DISTANCE);

        assertAll(
            () -> assertThat(response.getStations().stream()
                .mapToLong(value -> value.getId()))
                .containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId(), 판교역.getId(), 광교역.getId()),
            () -> assertThat(response.getDistance()).isEqualTo(58),
            () -> assertThat(response.getDuration()).isEqualTo(35),
            () -> assertThat(response.getFare()).isEqualTo(2150)
        );
    }

    @DisplayName("59km 거리의 경로조회를 한다.")
    @Test
    void findPathTest_7() {
        PathResponse response = pathService.findPath(교대역.getId(), 교판역.getId(), PathType.DISTANCE);

        assertAll(
            () -> assertThat(response.getStations().stream()
                .mapToLong(value -> value.getId()))
                .containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId(), 판교역.getId(), 광교역.getId(),
                    교판역.getId()),
            () -> assertThat(response.getDistance()).isEqualTo(59),
            () -> assertThat(response.getDuration()).isEqualTo(36),
            () -> assertThat(response.getFare()).isEqualTo(2250)
        );
    }
}