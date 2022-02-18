package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 관리")
@SpringBootTest
@Transactional
class PathServiceTest {

    private static final Fare 천원 = Fare.of(BigDecimal.valueOf(1_000));
    private static final Fare 이천원 = Fare.of(BigDecimal.valueOf(2_000));
    private static final Fare 삼천원 = Fare.of(BigDecimal.valueOf(3_000));

    @Autowired
    private LineService lineService;
    @Autowired
    private StationService stationService;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private PathService pathService;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService, stationService);

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        stationRepository.save(강남역);
        stationRepository.save(양재역);
        stationRepository.save(교대역);
        stationRepository.save(남부터미널역);

        신분당선 = Line.of("신분당선", "red", 삼천원);
        신분당선.addSection(강남역, 양재역, 10, 10);

        이호선 = Line.of("이호선", "red", 이천원);
        이호선.addSection(강남역, 교대역, 5, 5);

        삼호선 = Line.of("삼호선", "red", 천원);
        삼호선.addSection(교대역, 남부터미널역, 2, 2);
        삼호선.addSection(남부터미널역, 양재역, 2, 2);

        lineRepository.save(신분당선);
        lineRepository.save(이호선);
        lineRepository.save(삼호선);
    }


    @DisplayName("최단거리 경로 조회를 하면 경로, 거리, 시간 정보를 얻는다")
    @Test
    void findPath_short() {
        // when
        PathResponse response = pathService.findPath(교대역.getId(), 양재역.getId());

        // then
        List<String> 역이름 = response.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(역이름).contains(교대역.getName(), 남부터미널역.getName(), 양재역.getName());
        assertThat(response.getDistance()).isEqualTo(4);
        assertThat(response.getDuration()).isEqualTo(4);
    }

    @DisplayName("최단거리 경로의 요금 조회")
    @Test
    void findPath_distance() {
        // when
        PathResponse response = pathService.findPath(교대역.getId(), 양재역.getId());

        // then
        assertThat(response.getFare()).isEqualTo(BigDecimal.valueOf(2_250));
    }

}