package nextstep.core.subway.path.application;

import nextstep.common.annotation.ComponentTest;
import nextstep.core.subway.line.domain.Line;
import nextstep.core.subway.path.domain.PathFinderResult;
import nextstep.core.subway.path.domain.PathType;
import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.domain.Station;
import nextstep.core.subway.station.fixture.StationFixture;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("경로 조회 관련 테스트")
@ComponentTest
public class PathFinderTest {

    List<Line> 모든_노선_목록;

    Station 교대;
    Station 강남;
    Station 양재;
    Station 남부터미널;
    Station 정왕;
    Station 오이도;
    Station 가산디지털단지;
    Station 잠실;
    Station 천호;
    Station 신설동;

    Line 이호선;
    Line 신분당선;
    Line 삼호선;
    Line 사호선;
    Line 별내선;
    Line 일호선;

    private static PathFinder pathFinder;

    @BeforeAll
    static void 사전_객체_생성() {
        pathFinder = new PathFinder();
    }

    @BeforeEach
    void 사전_노선_설정() {

        이호선 = new Line("이호선", "green", 0);
        신분당선 = new Line("신분당선", "red", 400);
        삼호선 = new Line("삼호선", "orange", 800);
        사호선 = new Line("사호선", "blue", 600);
        별내선 = new Line("별내선", "pink", 200);
        일호선 = new Line("일호선", "blue", 300);

        모든_노선_목록 = List.of(이호선, 신분당선, 삼호선, 사호선, 별내선, 일호선);

        교대 = StationFixture.교대;
        ReflectionTestUtils.setField(교대, "id", 1L);

        강남 = StationFixture.강남;
        ReflectionTestUtils.setField(강남, "id", 2L);

        양재 = StationFixture.양재;
        ReflectionTestUtils.setField(양재, "id", 3L);

        남부터미널 = StationFixture.남부터미널;
        ReflectionTestUtils.setField(남부터미널, "id", 4L);

        정왕 = StationFixture.정왕;
        ReflectionTestUtils.setField(정왕, "id", 5L);

        오이도 = StationFixture.오이도;
        ReflectionTestUtils.setField(오이도, "id", 6L);

        가산디지털단지 = StationFixture.가산디지털단지;
        ReflectionTestUtils.setField(가산디지털단지, "id", 7L);

        잠실 = StationFixture.잠실;
        ReflectionTestUtils.setField(잠실, "id", 8L);

        천호 = StationFixture.천호;
        ReflectionTestUtils.setField(천호, "id", 9L);

        신설동 = StationFixture.신설동;
        ReflectionTestUtils.setField(신설동, "id", 10L);

    }

    @Nested
    class findOptimalPath {

        @Nested
        class 사전_노선_설정됨 {

            /**
             * <지하철 추가 정보>
             * <p>
             * 1호선: 300원
             * 2호선: 0원
             * 3호선: 800원
             * 4호선: 600원
             * 신분당선: 400원
             * 별내선: 200원
             * <p>
             * <지하철 노선도>
             * <p>
             * 교대역  --- *2호선*(10km, 5min, 0원) ---      강남역   ---(20km, 10min, 0원) ---  잠실역  --- *1호선(30km, 20min, 300원)* --- 신설동역
             * |                                             |                                 |
             * *3호선* (2km, 7min, 800원)          *신분당선*(10km, 3min, 400원)         *별내선*(5km, 5min)
             * |                                             |                                 |
             * 남부터미널역  --- *3호선*(3km, 3min, 800원) --- 양재역                           천호역
             * <p>
             * 오이도역 --- *4호선* --- 정왕역
             */
            @BeforeEach
            void 사전_노선_설정() {
                이호선.addSection(new Section(교대, 강남, 10, 5, 이호선));
                신분당선.addSection(new Section(강남, 양재, 10, 3, 신분당선));
                삼호선.addSection(new Section(교대, 남부터미널, 2, 7, 삼호선));
                사호선.addSection(new Section(정왕, 오이도, 10, 5, 사호선));
                별내선.addSection(new Section(잠실, 천호, 5, 5, 별내선));
                일호선.addSection(new Section(잠실, 신설동, 30, 20, 일호선));

                삼호선.addSection(new Section(남부터미널, 양재, 3, 3, 삼호선));
                이호선.addSection(new Section(강남, 잠실, 20, 10, 이호선));

            }

            @Nested
            class 최단거리_기준 {

                @Nested
                class 성공 {

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 경우
                     * Then  경로 내 존재하는 역, 이동 거리, 소요 시간, 운임 비용와 노선 추가 요금이 포함된 요금 정보를 확인할 수 있다.
                     */
                    @Test
                    void 강남역에서_남부터미널역까지_경로_조회() {
                        // when
                        PathFinderResult 경로_조회_결과 = pathFinder.findOptimalPath(모든_노선_목록, 강남, 남부터미널, PathType.DISTANCE);

                        // then
                        assertThat(경로_조회_결과).usingRecursiveComparison()
                                .isEqualTo(new PathFinderResult(List.of(강남, 교대, 남부터미널), 12, 12, List.of(0, 800)));
                    }

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 경우
                     * Then  경로 내 존재하는 역, 이동 거리, 소요 시간, 운임 비용와 노선 추가 요금이 포함된 요금 정보를 확인할 수 있다.
                     */
                    @Test
                    void 교대역에서_양재역까지_경로_조회() {
                        // when
                        PathFinderResult 경로_조회_결과 = pathFinder.findOptimalPath(모든_노선_목록, 교대, 양재, PathType.DISTANCE);

                        //
                        assertThat(경로_조회_결과).usingRecursiveComparison()
                                .isEqualTo(new PathFinderResult(List.of(교대, 남부터미널, 양재), 5, 10, List.of(800)));
                    }

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 경우
                     * Then  경로 내 존재하는 역, 이동 거리, 소요 시간, 운임 비용와 노선 추가 요금이 포함된 요금 정보를 확인할 수 있다.
                     */
                    @Test
                    void 양재역에서_신설동역까지_경로_조회() {
                        // when
                        PathFinderResult 경로_조회_결과 = pathFinder.findOptimalPath(모든_노선_목록, 양재, 신설동, PathType.DISTANCE);

                        //
                        assertThat(경로_조회_결과).usingRecursiveComparison()
                                .isEqualTo(new PathFinderResult(List.of(양재, 강남, 잠실, 신설동), 60, 33, List.of(400, 0, 300)));
                    }

                }

                @Nested
                class 실패 {

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 때,
                     * When     출발역과 도착역이 연결되어 있지 않을 경우
                     * Then  확인할 수 없다.
                     */
                    @Test
                    void 강남역에서_오이도역까지_경로_조회() {
                        // when, then
                        assertThatExceptionOfType(IllegalArgumentException.class)
                                .isThrownBy(() -> {
                                    pathFinder.findOptimalPath(모든_노선_목록, 강남, 오이도, PathType.DISTANCE);
                                })
                                .withMessageMatching("출발역과 도착역이 연결되어 있지 않습니다.");
                    }

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 때,
                     * When    출발역이 노선에 등록되어 있지 않을 경우
                     * Then  확인할 수 없다.
                     */
                    @Test
                    void 강남역에서_가산디지털단지역까지_경로_조회() {
                        // when, then
                        assertThatExceptionOfType(IllegalArgumentException.class)
                                .isThrownBy(() -> {
                                    pathFinder.findOptimalPath(모든_노선_목록, 가산디지털단지, 강남, PathType.DISTANCE);
                                })
                                .withMessageMatching("노선에 연결된 출발역이 아닙니다.");
                    }

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 때,
                     * When    도착역이 노선에 등록되어 있지 않을 경우
                     * Then  확인할 수 없다.
                     */
                    @Test
                    void 가산디지털단지역에서_강남역까지_경로_조회() {
                        // when, then
                        assertThatExceptionOfType(IllegalArgumentException.class)
                                .isThrownBy(() -> {
                                    pathFinder.findOptimalPath(모든_노선_목록, 강남, 가산디지털단지, PathType.DISTANCE);
                                })
                                .withMessageMatching("노선에 연결된 도착역이 아닙니다.");
                    }
                }
            }

            @Nested
            class 최소_소요시간_기준 {

                @Nested
                class 성공 {

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최소 소요시간 기준으로 경로를 조회할 경우
                     * Then  경로 내 존재하는 역, 이동 거리, 소요 시간, 운임 비용와 노선 추가 요금이 포함된 요금 정보를 확인할 수 있다.
                     */
                    @Test
                    void 강남역에서_남부터미널역까지_경로_조회() {
                        // when
                        PathFinderResult 경로_조회_결과 = pathFinder.findOptimalPath(모든_노선_목록, 강남, 남부터미널, PathType.DURATION);

                        // then
                        assertThat(경로_조회_결과).usingRecursiveComparison()
                                .isEqualTo(new PathFinderResult(List.of(강남, 양재, 남부터미널), 13, 6, List.of(400, 800)));
                    }

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최소 소요시간 기준으로 경로를 조회할 경우
                     * Then  경로 내 존재하는 역, 이동 거리, 소요 시간, 운임 비용와 노선 추가 요금이 포함된 요금 정보를 확인할 수 있다.
                     */
                    @Test
                    void 교대역에서_양재역까지_경로_조회() {
                        // when
                        PathFinderResult 경로_조회_결과 = pathFinder.findOptimalPath(모든_노선_목록, 교대, 양재, PathType.DURATION);

                        //
                        assertThat(경로_조회_결과).usingRecursiveComparison()
                                .isEqualTo(new PathFinderResult(List.of(교대, 강남, 양재), 20, 8, List.of(0, 400)));
                    }

                }

                @Nested
                class 실패 {

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최소 소요시간 기준으로 경로를 조회할 때,
                     * When     출발역과 도착역이 연결되어 있지 않을 경우
                     * Then  확인할 수 없다.
                     */
                    @Test
                    void 강남역에서_오이도역까지_경로_조회() {
                        // when, then
                        assertThatExceptionOfType(IllegalArgumentException.class)
                                .isThrownBy(() -> {
                                    pathFinder.findOptimalPath(모든_노선_목록, 강남, 오이도, PathType.DURATION);
                                })
                                .withMessageMatching("출발역과 도착역이 연결되어 있지 않습니다.");
                    }

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 때,
                     * When    출발역이 노선에 등록되어 있지 않을 경우
                     * Then  확인할 수 없다.
                     */
                    @Test
                    void 강남역에서_가산디지털단지역까지_경로_조회() {
                        // when, then
                        assertThatExceptionOfType(IllegalArgumentException.class)
                                .isThrownBy(() -> {
                                    pathFinder.findOptimalPath(모든_노선_목록, 가산디지털단지, 강남, PathType.DURATION);
                                })
                                .withMessageMatching("노선에 연결된 출발역이 아닙니다.");
                    }

                    /**
                     * Given 지하철 노선 목록이 생성된다.
                     * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 때,
                     * When    도착역이 노선에 등록되어 있지 않을 경우
                     * Then  확인할 수 없다.
                     */
                    @Test
                    void 가산디지털단지역에서_강남역까지_경로_조회() {
                        // when, then
                        assertThatExceptionOfType(IllegalArgumentException.class)
                                .isThrownBy(() -> {
                                    pathFinder.findOptimalPath(모든_노선_목록, 강남, 가산디지털단지, PathType.DURATION);
                                })
                                .withMessageMatching("노선에 연결된 도착역이 아닙니다.");
                    }
                }
            }
        }


        @Nested
        class 사전_노선_설정_안됨 {

            /**
             * Given 지하철 노선 목록이 생성된다.
             * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 때,
             * When     출발역과 도착역이 연결되어 있지 않을 경우
             * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 있다.
             */
            @Test
            void 최단거리_기준_강남역에서_남부터미널역까지_경로_조회() {
                // when, then
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            pathFinder.findOptimalPath(모든_노선_목록, 강남, 오이도, PathType.DISTANCE);
                        })
                        .withMessageMatching("노선에 연결된 구간이 하나라도 존재해야 합니다.");
            }

            /**
             * Given 지하철 노선 목록이 생성된다.
             * When  출발역과 도착역을 통해 최단거리 기준으로 경로를 조회할 때,
             * When     출발역과 도착역이 연결되어 있지 않을 경우
             * Then  최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 있다.
             */
            @Test
            void 최소_소요시간_기준_강남역에서_남부터미널역까지_경로_조회() {
                // when, then
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> {
                            pathFinder.findOptimalPath(모든_노선_목록, 강남, 오이도, PathType.DURATION);
                        })
                        .withMessageMatching("노선에 연결된 구간이 하나라도 존재해야 합니다.");
            }
        }
    }

    @Nested
    class isFoundPath {

        /**
         * 교대역    --- *2호선*(10km, 5min) ---   강남역
         * |                                       |
         * *3호선* (2km, 7min)                  *신분당선*(10km, 3min)
         * |                                       |
         * 남부터미널역  --- *3호선*(3km, 3min) ---양재역
         * <p>
         * 오이도역 --- *4호선* --- 정왕역
         */
        @BeforeEach
        void 사전_노선_설정() {
            이호선.addSection(new Section(교대, 강남, 10, 5, 이호선));
            신분당선.addSection(new Section(강남, 양재, 10, 3, 신분당선));
            삼호선.addSection(new Section(교대, 남부터미널, 2, 7, 삼호선));
            삼호선.addSection(new Section(남부터미널, 양재, 3, 3, 삼호선));
            사호선.addSection(new Section(정왕, 오이도, 10, 5, 사호선));
        }

        @Nested
        class 성공 {

            /**
             * Given 지하철 노선 목록이 생성된다.
             * When  출발역과 도착역을 잇는 경로가 존재할 경우
             * Then  경로가 존재한다는 응답이 출력된다.
             */
            @Test
            void 강남역에서_남부터미널역까지_경로_조회() {
                assertThat(pathFinder.existPathBetweenStations(모든_노선_목록, 강남, 남부터미널)).isTrue();
            }
        }

        @Nested
        class 실패 {
            /**
             * Given 지하철 노선 목록이 생성된다.
             * When  출발역과 도착역을 잇는 경로가 존재하지 않을 경우
             * Then  경로가 존재하지 않다는 응답이 출력된다.
             */
            @Test
            void 강남역에서_오이도까지_경로_조회() {
                assertThat(pathFinder.existPathBetweenStations(모든_노선_목록, 강남, 오이도)).isFalse();
            }
        }
    }
}
