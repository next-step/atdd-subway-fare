package nextstep.subway.unit;

import java.util.List;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private PathService pathService;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    /**
     * 교대역    --- *2호선* 10km   ---   강남역
     * |                                 |
     * *3호선* 900km                 *신분당선* 10km
     * |                                 |
     * 남부터미널역  --- *3호선* 10km ---   양재
     */
    @BeforeEach
    public void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        stationRepository.saveAll(List.of(교대역, 강남역, 양재역, 남부터미널역));

        이호선 = new Line("2호선", "red");
        신분당선 = new Line("신분당선", "orange");
        삼호선 = new Line("3호선", "green");
        lineRepository.saveAll(List.of(이호선, 신분당선, 삼호선));

        이호선.addSection(교대역, 강남역, 10);
        삼호선.addSection(교대역, 남부터미널역, 900);
        삼호선.addSection(남부터미널역, 양재역, 10);
        신분당선.addSection(강남역, 양재역, 10);
    }

    @DisplayName("거리 기준으로 경로를 조회한다.")
    @Test
    void findPath() {
        PathResponse path = pathService.findPath(교대역.getId(), 남부터미널역.getId());

        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(path.getDistance()).isEqualTo(30),
                () -> Assertions.assertThat(path.getStations()).extracting("name")
                        .containsExactly(교대역.getName(), 강남역.getName(), 양재역.getName(), 남부터미널역.getName())
        );
    }
}
