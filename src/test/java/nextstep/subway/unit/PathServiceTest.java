package nextstep.subway.unit;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.PathCondition;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationService stationService;

    @Autowired
    PathService pathService;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    /**
     * 교대역    --- *2호선(10d, 2s)* ---   강남역
     * |                               |
     * *3호선(2d, 10s)*                  *신분당선(10d, 3s)*
     * |                               |
     * 남부터미널역  --- *3호선(3d, 5s)* ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));

        이호선 = new Line("2호선", "green");
        신분당선 = new Line("신분당선", "red");
        삼호선 = new Line("3호선", "yellow");

        이호선.addSection(교대역, 강남역, 10, 2);
        삼호선.addSection(교대역, 남부터미널역, 2, 10);
        삼호선.addSection(남부터미널역, 양재역, 3, 5);
        신분당선.addSection(강남역, 양재역, 10, 3);

        lineRepository.save(이호선);
        lineRepository.save(신분당선);
        lineRepository.save(삼호선);
    }

    @DisplayName("최단경로를 거리 기준으로 찾는다")
    @Test
    public void find_shortest_path_by_distance() {
        // when
        PathResponse pathResponse = pathService.findPath(교대역.getId(), 양재역.getId(), PathCondition.DISTANCE);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).extracting("name").containsExactly("교대역", "남부터미널역", "양재역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getDuration()).isEqualTo(15),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("최단경로를 소요 시간 기준으로 찾는다")
    @Test
    public void find_shortest_path_by_duration() {
        // when
        PathResponse pathResponse = pathService.findPath(교대역.getId(), 양재역.getId(), PathCondition.DURATION);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).extracting("name").containsExactly("교대역", "강남역", "양재역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(20),
                () -> assertThat(pathResponse.getDuration()).isEqualTo(5),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1450)
        );
    }
}
