package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.marker.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.steps.LineSteps.createLines;
import static nextstep.subway.steps.PathSteps.getPath;
import static nextstep.subway.steps.PathSteps.verifyFoundPath;
import static nextstep.subway.steps.SectionSteps.createSection;
import static nextstep.subway.steps.StationSteps.createStation;
import static nextstep.utils.AcceptanceTestUtils.getId;
import static nextstep.utils.AcceptanceTestUtils.verifyResponseStatus;


@AcceptanceTest
public class PathAcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = getId(createStation("교대역"));
        강남역 = getId(createStation("강남역"));
        양재역 = getId(createStation("양재역"));
        남부터미널역 = getId(createStation("남부터미널역"));

        이호선 = getId(createLines("2호선", "green", 교대역, 강남역, 10L));
        신분당선 = getId(createLines("신분당선", "red", 강남역, 양재역, 10L));
        삼호선 = getId(createLines("3호선", "orange", 교대역, 남부터미널역, 2L));

        createSection(삼호선, 남부터미널역, 양재역, 3);
    }


    /**
     * Feature: 지하철 경로 검색
     * Scenario: 두 역의 최소 시간 경로를 조회
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * When 출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청
     * Then 최소 거리 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @Nested
    class Success_Ditance {

        @Test
        void 교대역에서_양재역을_가는_최단_경로는_교대_남부터미널_양재_5미터이다() {
            // when
            ValidatableResponse pathResponse = getPath(교대역, 양재역, PathType.DISTANCE);

            // then
            verifyFoundPath(pathResponse, 5L, 20, "교대역", "남부터미널역", "양재역");
        }

        @Test
        void 강남역에서_남부터미널역을_가는_최단_경로는_강남_교대_남부터미널_12미터이다() {
            // when
            ValidatableResponse pathResponse = getPath(강남역, 남부터미널역, PathType.DISTANCE);

            // then
            verifyFoundPath(pathResponse, 12L, 48L, "강남역", "교대역", "남부터미널역");
        }

        @Test
        void 강남역에서_양재역을_가는_최단_경로는_강남_양재_10미터이다() {
            // when
            ValidatableResponse pathResponse = getPath(강남역, 양재역, PathType.DISTANCE);

            // then
            verifyFoundPath(pathResponse, 10L, 40L, "강남역", "양재역");
        }

    }

    /**
     * Feature: 지하철 경로 검색
     * Scenario: 두 역의 최소 시간 경로를 조회
     * Given 지하철역이 등록되어있음
     * And 지하철 노선이 등록되어있음
     * And 지하철 노선에 지하철역이 등록되어있음
     * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
     * Then 최소 시간 기준 경로를 응답
     * And 총 거리와 소요 시간을 함께 응답함
     */
    @Nested
    class Success_Duration {

        @Test
        void 교대역에서_양재역을_가는_최단_경로는_교대_남부터미널_양재_5미터이다() {
            // when
            ValidatableResponse pathResponse = getPath(교대역, 양재역, PathType.DURATION);

            // then
            verifyFoundPath(pathResponse, 5L, 20, "교대역", "남부터미널역", "양재역");
        }

        @Test
        void 강남역에서_남부터미널역을_가는_최단_경로는_강남_교대_남부터미널_12미터이다() {
            // when
            ValidatableResponse pathResponse = getPath(강남역, 남부터미널역, PathType.DURATION);

            // then
            verifyFoundPath(pathResponse, 12L, 48, "강남역", "교대역", "남부터미널역");
        }

        @Test
        void 강남역에서_양재역을_가는_최단_경로는_강남_양재_10미터이다() {
            // when
            ValidatableResponse pathResponse = getPath(강남역, 양재역, PathType.DURATION);

            // then
            verifyFoundPath(pathResponse, 10L, 40,"강남역", "양재역");
        }

    }

    @Nested
    class Fail {

        @Test
        void 출발역과_도착역이_같은_경우() {
            // when
            ValidatableResponse pathResponse = getPath(강남역, 강남역);

            // then
            verifyResponseStatus(pathResponse, HttpStatus.BAD_REQUEST);
        }

        @Test
        void 출발역과_도착역이_연결되지_않은_경우() {
            // when
            Long 까치산역 = getId(createStation("까치산역"));
            ValidatableResponse pathResponse = getPath(까치산역, 강남역);

            // then
            verifyResponseStatus(pathResponse, HttpStatus.BAD_REQUEST);
        }

        @Test
        void 존재하지_않은_출발역이나_도착역을_조회_할_경우() {
            // when
            ValidatableResponse pathResponse = getPath(10000L, 강남역);

            // then
            verifyResponseStatus(pathResponse, HttpStatus.NOT_FOUND);
        }
    }
}
