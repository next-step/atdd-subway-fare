package nextstep.api.acceptance.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import nextstep.api.acceptance.AcceptanceTest;
import nextstep.api.acceptance.subway.station.StationSteps;
import nextstep.api.subway.applicaion.line.dto.request.LineCreateRequest;
import nextstep.api.subway.applicaion.line.dto.request.LineUpdateRequest;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @DisplayName("지하철노선을 생성한다")
    @Test
    void createLine() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                StationSteps.지하철역_생성_성공("강남역").getId(),
                StationSteps.지하철역_생성_성공("양재역").getId(),
                10,
                10
        );

        // when
        LineSteps.지하철노선_생성_성공(request);

        // then
        assertThat(LineSteps.지하철노선_전체조회_성공()).hasSize(1);
    }

    @DisplayName("모든 지하철노선 목록을 조회한다")
    @Test
    void showLines() {
        final var requests = List.of(
                new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        StationSteps.지하철역_생성_성공("강남역").getId(),
                        StationSteps.지하철역_생성_성공("양재역").getId(),
                        10,
                        10
                ),
                new LineCreateRequest(
                        "2호선",
                        "bg-green-600",
                        StationSteps.지하철역_생성_성공("강남역").getId(),
                        StationSteps.지하철역_생성_성공("역삼역").getId(),
                        10,
                        10
                )
        );

        // given
        LineSteps.지하철노선_생성_성공(requests);

        // when
        final var responses = LineSteps.지하철노선_전체조회_성공();

        // then
        assertThat(responses).hasSize(requests.size());
    }

    @DisplayName("지하철노선을 조회한다")
    @Test
    void findLine() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                StationSteps.지하철역_생성_성공("강남역").getId(),
                StationSteps.지하철역_생성_성공("양재역").getId(),
                10,
                10
        );

        // given
        final Long lineId = LineSteps.지하철노선_생성_성공(request).getId();

        // when
        final var response = LineSteps.지하철노선을_조회한다(lineId);

        // then
        assertThat(response.getId()).isPositive();
    }

    @DisplayName("지하철노선을 수정한다")
    @Nested
    class UpdateLineTest {

        @Nested
        class Success {

            @Test
            void 등록되어있는_지하철노선을_수정한다() {
                final var createRequest = new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        StationSteps.지하철역_생성_성공("강남역").getId(),
                        StationSteps.지하철역_생성_성공("양재역").getId(),
                        10,
                        10
                );
                final var updateRequest = new LineUpdateRequest("2호선", "bg-blue-123");

                // given
                final Long lineId = LineSteps.지하철노선_생성_성공(createRequest).getId();

                // when
                LineSteps.지하철노선_수정_성공(lineId, updateRequest);

                // then
                final var response = LineSteps.지하철노선을_조회한다(lineId);
                assertAll(
                        () -> assertThat(response.getName()).isEqualTo(updateRequest.getName()),
                        () -> assertThat(response.getColor()).isEqualTo(updateRequest.getColor())
                );
            }
        }

        @Nested
        class Fail {

            @DisplayName("지하철노선을 수정한다")
            @Test
            void 등록되지_않은_지하철노선을_수정한다() {
                LineSteps.지하철노선_수정_실패(0L, new LineUpdateRequest("2호선", "bg-blue-123"));
            }
        }
    }


    @DisplayName("지하철노선을 삭제한다")
    @Test
    void deleteLine() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                StationSteps.지하철역_생성_성공("강남역").getId(),
                StationSteps.지하철역_생성_성공("양재역").getId(),
                10,
                10
        );

        // given
        final Long lineId = LineSteps.지하철노선_생성_성공(request).getId();

        // when
        LineSteps.지하철노선_제거_성공(lineId);

        // then
        assertThat(LineSteps.지하철노선_전체조회_성공()).isEmpty();
    }
}
