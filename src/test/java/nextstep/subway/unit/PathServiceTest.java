package nextstep.subway.unit;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private PathService pathService;
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));

        final Line 이호선 = lineRepository.save(new Line("2호선", "green"));
        final Line 신분당선 = lineRepository.save(new Line("신분당선", "red"));
        final Line 삼호선 = lineRepository.save(new Line("3호선", "orange"));
        이호선.addSection(교대역, 강남역, 1, 10);
        신분당선.addSection(강남역, 양재역, 2, 9);
        삼호선.addSection(교대역, 남부터미널역, 3, 8);
        삼호선.addSection(남부터미널역, 양재역, 4, 7);
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void findPathWithDistance() {
        // when
        final PathResponse pathResponse = pathService.findPath(교대역.getId(), 양재역.getId(), FindType.DISTANCE);

        // then
        assertThat(pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList())).containsExactly(교대역.getId(), 강남역.getId(), 양재역.getId());
    }

    @DisplayName("최소 시간 경로 조회")
    @Test
    void findPathWithDuration() {
        // when
        final PathResponse pathResponse = pathService.findPath(교대역.getId(), 양재역.getId(), FindType.DURATION);

        // then
        assertThat(pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList())).containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId());
    }

    @DisplayName("최단 거리에 따른 요금 조회")
    @Test
    void getFareAboutShortestPath() {
        // when
        final PathResponse pathResponse = pathService.findPath(교대역.getId(), 양재역.getId(), FindType.DISTANCE);

        // then
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }
}
