package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("경로 서비스 단위 테스트")
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineService lineService;
    @Autowired
    private PathService pathService;

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;

    @BeforeEach
    void setUp() {
        setUpStation();
        setUpLine();
    }

    @DisplayName("소요 시간에 따른 최단 경로를 조회한다")
    @Test
    void shortestPathByDuration() {
        // when
        PathResponse response = pathService.findShortestPath(남부터미널역.getId(), 강남역.getId(), "DURATION");

        // then
        Assertions.assertThat(response.getDuration()).isEqualTo(4);
    }

    private void setUpStation() {
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
    }

    private void setUpLine() {
        Long 이호선 = lineService.saveLine(
                new LineRequest("2호선", "green", 교대역.getId(), 강남역.getId(), 1, 2)).getId();
        Long 신분당선 = lineService.saveLine(
                new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 2, 4)).getId();
        Long 삼호선 = lineService.saveLine(
                new LineRequest("3호선", "orange", 교대역.getId(), 남부터미널역.getId(), 1, 2)).getId();
        lineService.addSection(삼호선, new SectionRequest(남부터미널역.getId(), 양재역.getId(), 1, 2));
    }
}