package nextstep.api.acceptance.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import static nextstep.api.acceptance.subway.line.SectionSteps.지하철구간_등록_성공;

import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import nextstep.api.acceptance.AcceptanceTest;
import nextstep.api.acceptance.subway.line.LineSteps;
import nextstep.api.acceptance.subway.station.StationSteps;
import nextstep.api.subway.applicaion.line.dto.request.SectionRequest;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.api.subway.domain.path.PathSelection;

@DisplayName("지하철 경로 관리 기능")
class PathAcceptanceTest extends AcceptanceTest {

    private Long 교대역, 강남역, 양재역, 남부터미널역, 광교역;

    //
    //   교대역    --- 10 --- 강남역
    //     |                   |
    //     5                   5
    //     |                   |
    // 남부터미널역  --- 5 --- 양재역
    //

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = StationSteps.지하철역_생성_성공("교대역").getId();
        강남역 = StationSteps.지하철역_생성_성공("강남역").getId();
        양재역 = StationSteps.지하철역_생성_성공("양재역").getId();
        남부터미널역 = StationSteps.지하철역_생성_성공("남부터미널역").getId();
        광교역 = StationSteps.지하철역_생성_성공("광교역").getId();

        LineSteps.지하철노선_생성_성공(교대역, 강남역, 10, 10, 0);
        LineSteps.지하철노선_생성_성공(강남역, 양재역, 5, 5, 0);
        지하철구간_등록_성공(LineSteps.지하철노선_생성_성공(교대역, 남부터미널역, 5, 5, 0).getId(), new SectionRequest(남부터미널역, 양재역, 5, 5));
    }

    @DisplayName("지하철 최단경로를 조회한다")
    @Nested
    class showShortestPath {

        @Nested
        class Success {

            @Test
            void 거리_기준_최단경로를_조회한다() {
                // when
                final var response = PathSteps.최단경로조회_성공(교대역, 양재역, PathSelection.DISTANCE);

                // then
                final var distance = response.getDistance();
                final var stations = response.getStations().stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toUnmodifiableList());

                assertAll(
                        () -> assertThat(distance).isEqualTo(10),
                        () -> assertThat(stations).containsExactly(교대역, 남부터미널역, 양재역)
                );
            }

            @Test
            void 소요시간_기준_최단경로를_조회한다() {
                // when
                final var response = PathSteps.최단경로조회_성공(교대역, 양재역, PathSelection.DURATION);

                // then
                final var duration = response.getDuration();
                final var stations = response.getStations().stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toUnmodifiableList());

                assertAll(
                        () -> assertThat(duration).isEqualTo(10),
                        () -> assertThat(stations).containsExactly(교대역, 남부터미널역, 양재역)
                );
            }
        }

        @Nested
        class Fail {

            @Test
            void 출발역과_도착역이_같아선_안된다() {
                PathSteps.최단경로조회_실패(강남역, 강남역, PathSelection.DURATION, HttpStatus.BAD_REQUEST);
            }

            @Test
            void 출발역과_도착역이_연결되어_있어야_한다() {
                PathSteps.최단경로조회_실패(강남역, 광교역, PathSelection.DURATION, HttpStatus.BAD_REQUEST);
            }

            @Test
            void 존재하지_않는_역은_출발역이_될_수_없다() {
                PathSteps.최단경로조회_실패(0L, 강남역, PathSelection.DURATION, HttpStatus.BAD_REQUEST);
            }

            @Test
            void 존재하지_않는_역은_도착역이_될_수_없다() {
                PathSteps.최단경로조회_실패(강남역, 0L, PathSelection.DURATION, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
