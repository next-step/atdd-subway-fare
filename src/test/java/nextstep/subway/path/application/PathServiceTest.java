package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.repository.LineRepository;
import nextstep.subway.member.domain.EmptyMember;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.repository.StationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class PathServiceTest {
    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    PathService pathService;

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;

    EmptyMember emptyMember;

    /**          (40km, 33min)
     * 교대역    --- *2호선* ---   강남역
     *   |                          |
     * (33km, 40min)             (40km, 33min)
     * *3호선*                   *신분당선*
     *   |                          |
     * 남부터미널역  --- *3호선* --- 양재
     *               (33km, 40min)
     */
    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        stationRepository.saveAll(Arrays.asList(교대역, 강남역, 양재역, 남부터미널역));

        Line 이호선 = new Line("2호선", "green", 500);
        이호선.addSection(교대역, 강남역, 40, 33);

        Line 삼호선 = new Line("3호선", "orange", 1100);
        삼호선.addSection(교대역, 남부터미널역, 33, 40);
        삼호선.addSection(남부터미널역, 양재역, 33, 40);

        Line 신분당선 = new Line("신분당선", "red", 1500);
        신분당선.addSection(강남역, 양재역, 40, 33);
        lineRepository.saveAll(Arrays.asList(이호선, 삼호선, 신분당선));

        emptyMember = new EmptyMember();
    }

    @DisplayName("거리기준으로 최소 경로를 구한다")
    @Test
    void findPathByDistance() {
        // when
        PathResponse response = pathService.findPath(emptyMember.getAge(), 교대역.getId(), 양재역.getId(), PathType.DISTANCE);

        // then
        assertAll(() -> {
            assertThat(response.getStations()).extracting(StationResponse::getId)
                    .containsExactly(교대역.getId(), 남부터미널역.getId(), 양재역.getId());
            assertThat(response.getDistance()).isEqualTo(66);
            assertThat(response.getFare()).isEqualTo(3350);
        });
    }

    @DisplayName("시간기준으로 최소 경로를 구한다")
    @Test
    void findPathByDuration() {
        // when
        PathResponse response = pathService.findPath(emptyMember.getAge(), 교대역.getId(), 양재역.getId(), PathType.DURATION);

        // then
        List<Long> stationIds = response.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(stationIds).containsExactly(교대역.getId(), 강남역.getId(), 양재역.getId());
            assertThat(response.getDuration()).isEqualTo(66);
            assertThat(response.getFare()).isEqualTo(3950);
        });
    }

    @ParameterizedTest
    @CsvSource({"6,1500", "13,2400", "19,3350"})
    void findPathByDistanceWithLogin(int age, int expectedFare) {
        // when
        PathResponse response = pathService.findPath(age, 교대역.getId(), 양재역.getId(), PathType.DISTANCE);

        // then
        assertThat(response.getFare()).isEqualTo(expectedFare);
    }

}
