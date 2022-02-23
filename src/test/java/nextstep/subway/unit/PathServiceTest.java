package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.domain.fare.MemberDiscountPolicy.ADULT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PathServiceTest {
    public static int NON_LOGIN_AGE = 20;

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    PathService pathService;

    Long 교대역;
    Long 강남역;
    Long 양재역;
    Long 남부터미널역;

    Long 이호선;
    Long 신분당선;
    Long 삼호선;

    /**
     * 노선(거리, 시간)
     *
     * 교대역 > 양재역
     * 최단 거리: 5(교대역 - 남부터미널역 - 양재역)
     * 최소 시간: 6(교대역 - 강남역 - 양재역)
     *
     * 교대역      ---- *2호선(10, 3)*    ---- 강남역
     * |                                |
     * *3호선(2, 10)*                    *신분당선(10, 3)*
     * |                                |
     * 남부터미널역  ---- *3호선(3, 10)* ----  양재
     */
    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService, stationService);

        교대역 = stationService.saveStation(new StationRequest("교대역")).getId();
        강남역 = stationService.saveStation(new StationRequest("강남역")).getId();
        양재역 = stationService.saveStation(new StationRequest("양재역")).getId();
        남부터미널역 = stationService.saveStation(new StationRequest("남부터미널역")).getId();

        이호선 = lineService.saveLine(new LineRequest("2호선", "green", 교대역, 강남역, 10, 3)).getId();
        신분당선 = lineService.saveLine(new LineRequest("신분당선", "red", 강남역, 양재역, 10, 3)).getId();
        삼호선 = lineService.saveLine(new LineRequest("3호선", "orange", 교대역, 남부터미널역, 2, 10)).getId();
        lineService.addSection(삼호선, new SectionRequest(남부터미널역, 양재역, 3, 10));
    }

    @Test
    void findPathMinDistance() {
        // when
        PathResponse response = pathService.findPath(교대역, 양재역, PathType.DISTANCE, ADULT.getAge());

        // then
        List<Long> ids = response.getStations().stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(ids).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
        assertThat(response.getDistance()).isEqualTo(5);
    }

    @Test
    void findPathMinDuration() {
        // when
        PathResponse response = pathService.findPath(교대역, 양재역, PathType.DURATION, ADULT.getAge());

        // then
        List<Long> ids = response.getStations().stream().map(StationResponse::getId).collect(Collectors.toList());
        assertThat(ids).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
        assertThat(response.getDuration()).isEqualTo(6);
    }
}