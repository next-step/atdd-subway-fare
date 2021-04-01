package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.acceptance.documentation.PathDocumentation;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.AcceptanceTest;
import nextstep.subway.utils.BaseDocumentSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.Arrays;

import static nextstep.subway.line.acceptance.line.LineRequestSteps.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.linesection.LineSectionRequestSteps.노선_요청;
import static nextstep.subway.line.acceptance.linesection.LineSectionRequestSteps.지하철_노선에_구간_등록_요청;
import static nextstep.subway.path.acceptance.PathRequestSteps.지하철_최단_거리_및_최소_시간_경로_조회_요청;
import static nextstep.subway.path.acceptance.PathVerificationSteps.*;
import static nextstep.subway.station.acceptance.StationRequestSteps.지하철_역_등록_됨;
import static nextstep.subway.utils.BaseDocumentSteps.givenDefault;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final String DOCUMENT_IDENTIFIER_PATH = "path/{method-name}";

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 삼성역;
    private StationResponse 양재역;
    private StationResponse 양재시민의숲역;
    private StationResponse 청계산입구역;
    private StationResponse 남부터미널역;

    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;

    private BaseDocumentSteps baseDocumentSteps;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        super.setUp(restDocumentation);

        교대역 = 지하철_역_등록_됨("교대역").as(StationResponse.class);
        강남역 = 지하철_역_등록_됨("강남역").as(StationResponse.class);
        역삼역 = 지하철_역_등록_됨("역삼역").as(StationResponse.class);
        삼성역 = 지하철_역_등록_됨("삼성역").as(StationResponse.class);
        양재역 = 지하철_역_등록_됨("양재역").as(StationResponse.class);
        양재시민의숲역 = 지하철_역_등록_됨("양재시민의숲역").as(StationResponse.class);
        청계산입구역 = 지하철_역_등록_됨("청계산입구역").as(StationResponse.class);
        남부터미널역 = 지하철_역_등록_됨("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_생성_요청(givenDefault(), 노선_요청("신분당선", "green", 강남역.getId(), 양재역.getId(), 5, 5)).as(LineResponse.class);
        지하철_노선에_구간_등록_요청(givenDefault(), 신분당선.getId(), 양재역.getId(), 양재시민의숲역.getId(), 3, 3);
        지하철_노선에_구간_등록_요청(givenDefault(), 신분당선.getId(), 양재시민의숲역.getId(), 청계산입구역.getId(), 4, 7);

        이호선 = 지하철_노선_생성_요청(givenDefault(), 노선_요청("2호선", "green", 교대역.getId(), 강남역.getId(), 7, 7)).as(LineResponse.class);
        지하철_노선에_구간_등록_요청(givenDefault(), 이호선.getId(), 강남역.getId(), 역삼역.getId(), 5, 5);
        지하철_노선에_구간_등록_요청(givenDefault(), 이호선.getId(), 역삼역.getId(), 삼성역.getId(), 8, 8);

        삼호선 = 지하철_노선_생성_요청(givenDefault(), 노선_요청("3호선", "green", 교대역.getId(), 남부터미널역.getId(), 3, 3)).as(LineResponse.class);
        지하철_노선에_구간_등록_요청(givenDefault(), 삼호선.getId(), 남부터미널역.getId(), 양재역.getId(), 3, 3);
    }

    @Test
    @DisplayName("지하철 최단 거리 경로 조회")
    void findPathByDistance() {
        // given
        baseDocumentSteps = new PathDocumentation(spec);
        RequestSpecification 최단_경로_탐색_문서화_요청 = baseDocumentSteps.requestDocumentOfFind(DOCUMENT_IDENTIFIER_PATH);

        // when
        ExtractableResponse<Response> response = 지하철_최단_거리_및_최소_시간_경로_조회_요청(최단_경로_탐색_문서화_요청, 강남역.getId(), 양재역.getId(), PathType.DISTANCE);

        // then
        지하철_최단_경로_조회_됨(response, Arrays.asList(강남역.getId(), 양재역.getId()));
        경로_조회_거리와_소요_시간_응답_됨(response, 5, 5);
        지하철_역_경로_조회_요금_확인(response, 1250);
    }

    @Test
    @DisplayName("지하철 최소 시간 경로 조회")
    void findPathByDuration() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_거리_및_최소_시간_경로_조회_요청(givenDefault(), 삼성역.getId(), 남부터미널역.getId(), PathType.DURATION);

        // then
        지하철_최단_경로_조회_됨(response, Arrays.asList(삼성역.getId(), 역삼역.getId(), 강남역.getId(), 양재역.getId(), 남부터미널역.getId()));
        경로_조회_거리와_소요_시간_응답_됨(response, 21, 21);
        지하철_역_경로_조회_요금_확인(response, 1550);
    }

    @Test
    @DisplayName("지하철 최단 거리 경로 조회 - 추가요금이 있는 신분당선을 경유할 경우 (10Km 이하)")
    void findPathByDistanceToNewBunDang1() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_거리_및_최소_시간_경로_조회_요청(givenDefault(), 양재역.getId(), 청계산입구역.getId(), PathType.DISTANCE);

        // then
        지하철_최단_경로_조회_됨(response, Arrays.asList(역삼역.getId(), 강남역.getId(), 양재역.getId(), 양재시민의숲역.getId()));
        경로_조회_거리와_소요_시간_응답_됨(response, 7, 10);
        지하철_역_경로_조회_요금_확인(response, 2150);
    }

    @Test
    @DisplayName("지하철 최단 거리 경로 조회 - 추가요금이 있는 신분당선을 경유할 경우 (10Km 초과)")
    void findPathByDistanceToNewBunDang2() {
        // when
        ExtractableResponse<Response> response = 지하철_최단_거리_및_최소_시간_경로_조회_요청(givenDefault(), 역삼역.getId(), 양재시민의숲역.getId(), PathType.DISTANCE);

        // then
        지하철_최단_경로_조회_됨(response, Arrays.asList(역삼역.getId(), 강남역.getId(), 양재역.getId(), 양재시민의숲역.getId()));
        경로_조회_거리와_소요_시간_응답_됨(response, 13, 13);
        지하철_역_경로_조회_요금_확인(response, 2250);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외 발생")
    void notEqualsSourceAndTarget() {
        // given & when
        ExtractableResponse<Response> response = 지하철_최단_거리_및_최소_시간_경로_조회_요청(givenDefault(), 강남역.getId(), 강남역.getId(), PathType.DISTANCE);

        // then
        지하철_최단_경로_조회_실패_됨(response);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    void notConnectedSourceAndTarget() {
        // given
        StationResponse 명동역 = 지하철_역_등록_됨("명동역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_최단_거리_및_최소_시간_경로_조회_요청(givenDefault(), 강남역.getId(), 명동역.getId(), PathType.DISTANCE);

        // then
        지하철_최단_경로_조회_실패_됨(response);
    }

    @Test
    @DisplayName("존재하지 않는 출발역, 도착역을 조회할 경우 예외 발생")
    void findNotExistSourceAndTarget() {
        // given & when
        ExtractableResponse<Response> response = PathRequestSteps.지하철_최단_거리_및_최소_시간_경로_조회_요청(givenDefault(), 100L, 101L, PathType.DISTANCE);

        // then
        지하철_역_조회_실패_됨(response);
    }
}
