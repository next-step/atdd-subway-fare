package nextstep.core.subway.path.acceptance;

import nextstep.common.utils.AcceptanceTest;
import nextstep.core.subway.station.fixture.StationFixture;
import nextstep.core.subway.path.domain.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.core.subway.line.fixture.LineFixture.*;
import static nextstep.core.subway.line.step.LineSteps.지하철_노선_생성;
import static nextstep.core.subway.path.fixture.PathFixture.지하철_경로;
import static nextstep.core.subway.path.step.PathSteps.*;
import static nextstep.core.subway.section.fixture.SectionFixture.지하철_구간;
import static nextstep.core.subway.section.step.SectionSteps.성공하는_지하철_구간_추가요청;
import static nextstep.core.subway.station.step.StationSteps.지하철_역_생성;

@DisplayName("경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    Long 교대역;
    Long 강남역;
    Long 양재역;
    Long 남부터미널역;
    Long 정왕역;
    Long 오이도역;

    Long 존재하지_않는_역 = 999L;

    Long 이호선;
    Long 신분당선;
    Long 삼호선;
    Long 사호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     * <p>
     * 오이도역 --- *4호선* --- 정왕역
     */
    @BeforeEach
    public void 사전_노선_설정() {
        교대역 = 지하철_역_생성(StationFixture.교대역);
        강남역 = 지하철_역_생성(StationFixture.강남역);
        양재역 = 지하철_역_생성(StationFixture.양재역);
        남부터미널역 = 지하철_역_생성(StationFixture.남부터미널역);
        정왕역 = 지하철_역_생성(StationFixture.정왕역);
        오이도역 = 지하철_역_생성(StationFixture.오이도역);

        이호선 = 지하철_노선_생성(이호선(교대역, 강남역, 10, 10, 10));
        신분당선 = 지하철_노선_생성(신분당선(강남역, 양재역, 10, 10, 10));
        삼호선 = 지하철_노선_생성(삼호선(교대역, 남부터미널역, 2, 2, 10));
        사호선 = 지하철_노선_생성(사호선(정왕역, 오이도역, 10, 10, 10));

        성공하는_지하철_구간_추가요청(삼호선, 지하철_구간(남부터미널역, 양재역, 3, 3));
    }

    @Nested
    class 성공 {

        /**
         * Given 지하철 노선을 생성하고, 구간을 추가한다.
         * When  출발역과 도착역을 통해 경로를 조회할 경우
         * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 있다.
         */
        @Test
        void 강남역에서_남부터미널역까지_경로_조회() {
            // when
            var 성공하는_경로_조회_응답 = 성공하는_지하철_경로_조회_요청(지하철_경로(강남역, 남부터미널역, PathType.DISTANCE.name()));

            // then
            경로에_포함된_역_목록_검증(성공하는_경로_조회_응답, 강남역, 교대역, 남부터미널역);
            경로에_포함된_최단거리_검증(성공하는_경로_조회_응답, 12);
        }

        /**
         * Given 지하철 노선을 생성하고, 구간을 추가한다.
         * When  출발역과 도착역을 통해 경로를 조회할 경우
         * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 있다.
         */
        @Test
        void 교대역에서_양재역까지_경로_조회() {
            // when
            var 성공하는_경로_조회_응답 = 성공하는_지하철_경로_조회_요청(지하철_경로(교대역, 양재역,  PathType.DISTANCE.name()));

            // then
            경로에_포함된_역_목록_검증(성공하는_경로_조회_응답, 교대역, 남부터미널역, 양재역);
            경로에_포함된_최단거리_검증(성공하는_경로_조회_응답, 5);
        }

    }

    @Nested
    class 실패 {

        @Nested
        class 출발역과_도착역_동일 {

            /**
             * Given 지하철 노선을 생성하고, 구간을 추가한다.
             * When  출발역과 도착역을 통해 경로를 조회할 때,
             * When     출발역과 도착역이 동일할 경우
             * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 없다.
             */
            @Test
            void 강남역에서_강남역까지_경로_조회() {
                // when,then
                실패하는_지하철_경로_조회_요청(지하철_경로(강남역, 강남역, PathType.DISTANCE.name()));
            }

        }

        @Nested
        class 출발역과_도착역이_연결되지_않음 {

            /**
             * Given 지하철 노선을 생성하고, 구간을 추가한다.
             * When  출발역과 도착역을 통해 경로를 조회할 때,
             * When     출발역과 도착역이 연결되어 있지 않을 경우
             * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 없다.
             */
            @Test
            void 강남역에서_오이도역까지_경로_조회() {
                // when,then
                실패하는_지하철_경로_조회_요청(지하철_경로(강남역, 오이도역, PathType.DISTANCE.name()));

            }

            @Nested
            class 출발역_혹은_도착역이_없음 {

                /**
                 * Given 지하철 노선을 생성하고, 구간을 추가한다.
                 * When  출발역과 도착역을 통해 경로를 조회할 때,
                 * When     출발역이 존재하지 않을 경우
                 * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 없다.
                 */
                @Test
                void 강남역에서_존재하지_않는_역까지_경로_조회() {
                    // when,then
                    실패하는_지하철_경로_조회_요청(지하철_경로(강남역, 존재하지_않는_역, PathType.DISTANCE.name()));
                }


                /**
                 * Given 지하철 노선을 생성하고, 구간을 추가한다.
                 * When  출발역과 도착역을 통해 경로를 조회할 때,
                 * When     도착역이 존재하지 않을 경우
                 * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 없다.
                 */
                @Test
                void 존재하지_않는_역에서_강남역까지_경로_조회() {
                    // when,then
                    실패하는_지하철_경로_조회_요청(지하철_경로(존재하지_않는_역, 강남역, PathType.DISTANCE.name()));
                }

            }
        }
    }
}
