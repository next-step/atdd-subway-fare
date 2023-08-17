package nextstep.subway.unit;


import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.constant.FindPathType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@DisplayName("최단 경로 조회 테스트")
class PathServiceTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private PathService pathService;


    private Line 이호선, 신분당선, 삼호선, 수인분당선;
    private Station 교대역, 강남역, 양재역, 남부터미널역, 선릉역, 도곡역;

    @BeforeEach
    void setup() {
        교대역 = createStation("교대역");
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        남부터미널역 = createStation("남부터미널역");
        이호선 = createLine("2호선", "green");
        이호선.addSection(교대역, 강남역,  1, 10);
        lineRepository.save(이호선);
        신분당선 = createLine("신분당선", "green");
        lineRepository.save(신분당선);
        신분당선.getSections().add(createSection(신분당선, 강남역, 양재역, 10, 11));
        삼호선 = createLine("3호선", "green");
        lineRepository.save(삼호선);
        삼호선.getSections().add(createSection(삼호선, 교대역, 남부터미널역, 2, 12));
        삼호선.getSections().add(createSection(삼호선, 남부터미널역, 양재역, 3, 13));
        lineRepository.flush();
    }

    /**
     * <pre>
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * </pre>
     */
    @DisplayName("최단 거리 경로 조회 - 교대, 남부터미널, 양재")
    @Test
    void findPathPathTypeDistance() {
        // given : 선행조건 기술

        // when : 기능 수행
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DISTANCE);

        // then : 결과 확인
        assertThat(path.getStations()).hasSize(3)
                .extracting("name").containsExactly("교대역", "남부터미널역", "양재역");
        assertThat(path.getDistance()).isEqualTo(5);
        assertThat(path.getDuration()).isEqualTo(25);
    }

    /**
     * <pre>
     * 교대역    --- *2호선* ---   강남역  --- *2호선* ---    선릉
     * |                        |                       |
     * *3호선*                   *신분당선*               *수인분당*
     * |                        |                       |
     * 남부터미널역  --- *3호선* ---   양재   --- *3호선* --- 도곡
     * </pre>
     */
    @DisplayName("최단 거리 경로 조회2 - 교대, 강남, 선릉, 도곡, 양재역")
    @Test
    void findPathPathTypeDistance2() {
        // given : 선행조건 기술
        선릉역 = createStation("선릉");
        도곡역 = createStation("도곡");
        수인분당선 = createLine("수인분당", "green");
        수인분당선.getSections().add(createSection(수인분당선, 선릉역, 도곡역, 1, 1));
        이호선.getSections().add(createSection(이호선, 강남역, 선릉역, 1, 2));
        삼호선.getSections().add(createSection(삼호선, 양재역, 도곡역, 1, 3));
        lineRepository.save(수인분당선);
        lineRepository.flush();

        // when : 기능 수행
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DISTANCE);

        // then : 결과 확인
        assertThat(path.getStations()).hasSize(5)
                .extracting("name").containsExactly("교대역", "강남역", "선릉", "도곡", "양재역");
        assertThat(path.getDistance()).isEqualTo(4);
        assertThat(path.getDuration()).isEqualTo(16);
    }

    /**
     * <pre>
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * </pre>
     */
    @DisplayName("최소 시간 경로 조회 - 교대, 강남, 양재")
    @Test
    void findPathTypeDuration() {
        // given : 선행조건 기술

        // when : 기능 수행
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DURATION);

        // then : 결과 확인
        assertThat(path.getStations()).hasSize(3)
                .extracting("name").containsExactly("교대역", "강남역", "양재역");
        assertThat(path.getDistance()).isEqualTo(11);
        assertThat(path.getDuration()).isEqualTo(21);
    }

    /**
     * <pre>
     * 교대역    --- *2호선* ---   강남역  --- *2호선* ---    선릉
     * |                        |                       |
     * *3호선*                   *신분당선*               *수인분당*
     * |                        |                       |
     * 남부터미널역  --- *3호선* ---   양재   --- *3호선* --- 도곡
     * </pre>
     */
    @DisplayName("최소 시간 경로 조회2 - 교대, 강남, 선릉, 도곡, 양재역")
    @Test
    void findPathTypeDuration2() {
        // given : 선행조건 기술
        선릉역 = createStation("선릉");
        도곡역 = createStation("도곡");
        수인분당선 = createLine("수인분당", "green");
        수인분당선.getSections().add(createSection(수인분당선, 선릉역, 도곡역, 1, 1));
        이호선.getSections().add(createSection(이호선, 강남역, 선릉역, 1, 2));
        삼호선.getSections().add(createSection(삼호선, 양재역, 도곡역, 1, 3));
        lineRepository.save(수인분당선);
        lineRepository.flush();

        // when : 기능 수행
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DISTANCE);

        // then : 결과 확인
        assertThat(path.getStations()).hasSize(5)
                .extracting("name").containsExactly("교대역", "강남역", "선릉", "도곡", "양재역");
        assertThat(path.getDistance()).isEqualTo(4);
        assertThat(path.getDuration()).isEqualTo(16);
    }

    /**
     * <pre>
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * </pre>
     */
    @DisplayName("최단 거리 경로 조회 및 기본 요금 응답 확인 - 교대, 남부터미널, 양재")
    @Test
    void findPathPathTypeDistanceReturnFare() {
        // given : 선행조건 기술

        // when : 기능 수행
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DISTANCE);

        // then : 결과 확인
        assertThat(path.getStations()).hasSize(3)
                .extracting("name").containsExactly("교대역", "남부터미널역", "양재역");
        assertThat(path.getDistance()).isEqualTo(5);
        assertThat(path.getDuration()).isEqualTo(25);
        assertThat(path.getFare()).isEqualTo(1250);
    }

    private Section createSection(Line line, Station upStation, Station downStation, int distance, int duration) {
        return new Section(line, upStation, downStation, distance, duration);
    }

    private Station createStation(String name) {
        return new Station(name);
    }

    private Line createLine(String name, String color) {
        return new Line(name, color);
    }
}