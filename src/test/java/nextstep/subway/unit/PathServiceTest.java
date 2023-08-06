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
class PathServiceTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private PathService pathService;


    private Line line, line2, line3;
    private Station 교대역, 강남역, 양재역, 남부터미널역;

    @BeforeEach
    void setup() {
        교대역 = createStation("교대역");
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        남부터미널역 = createStation("남부터미널역");
        line = createLine("2호선", "green");
        line.addSection(교대역, 강남역,  10, 10);
        lineRepository.save(line);

        line2 = createLine("신분당선", "green");
        lineRepository.save(line2);
        line2.getSections().add(createSection(line2, 강남역, 양재역, 10, 11));
        line3 = createLine("3호선", "green");
        lineRepository.save(line3);
        line3.getSections().add(createSection(line3, 교대역, 남부터미널역, 2, 12));
        line3.getSections().add(createSection(line3, 남부터미널역, 양재역, 3, 13));
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
    @DisplayName("최단거리 경로 조회")
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
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * </pre>
     */
    @DisplayName("최단거리 경로 조회")
    @Test
    void findPathTypeDuration() {
        // given : 선행조건 기술

        // when : 기능 수행
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindPathType.DURATION);

        // then : 결과 확인
        assertThat(path.getStations()).hasSize(3)
                .extracting("name").containsExactly("교대역", "강남역", "양재역");
        assertThat(path.getDistance()).isEqualTo(20);
        assertThat(path.getDuration()).isEqualTo(21);
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