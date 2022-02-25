package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    public static ExtractableResponse<Response> 경로조회_회원(String accessToken , Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 경로조회_비회원(Map<String, String> params) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 경로조회_회원_문서화(String accessToken ,RequestSpecification spec, RestDocumentationFilter filter, Map<String, String> params) {
        return RestAssured
                .given(spec).log().all()
                .auth().oauth2(accessToken)
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 경로조회_비회원_문서화(RequestSpecification spec, RestDocumentationFilter filter, Map<String, String> params) {
        return RestAssured
                .given(spec).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all().extract();
    }


    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        return 지하철_노선_생성_요청(name, color, upStation, downStation, distance, duration, 0);
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration, int additionalFee) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");
        lineCreateParams.put("additionalFee", additionalFee + "");
        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }


    public static ResponseFieldsSnippet getResponseFields() {
        ResponseFieldsSnippet responseFieldsSnippet = responseFields(
                fieldWithPath("stations").description("해당 경로에 포함된 역 목록"),
                fieldWithPath("stations[].id").description("지하철 역의 ID (Long)"),
                fieldWithPath("stations[].name").description("지하철 역 이름"),
                fieldWithPath("stations[].createdDate").description("해당 역 생성 일"),
                fieldWithPath("stations[].modifiedDate").description("해당 역 마지막 수정 일"),
                fieldWithPath("distance").description("해당 경로 사이의 거리"),
                fieldWithPath("duration").description("해당 경로 소요 시간"),
                fieldWithPath("fee").description("해당 경로의 금액")
        );
        return responseFieldsSnippet;
    }

    public static RequestParametersSnippet getRequestParameters() {
        RequestParametersSnippet requestParametersSnippet = requestParameters(
                parameterWithName("type").description("조회하는 방식 (최단 거리 or 최소 시간)"),
                parameterWithName("source").description("조회하는 경로에 상행역"),
                parameterWithName("target").description("조회하는 경로에 하행역")
        );
        return requestParametersSnippet;
    }

    public static PathResponse getPathResponseForAnonymous() {
        PathResponse pathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 3, 1350
        );
        return pathResponse;
    }

    public static PathResponse getPathResponseForUser() {
        PathResponse pathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 3, 800
        );
        return pathResponse;
    }

    public static void 경로조회_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2);
        assertThat(response.jsonPath().getList("stations.name")).containsExactly("강남역", "역삼역");
    }

    public static RestDocumentationFilter 경로관련_문서_필터생성(String identifier) {
        RequestParametersSnippet requestParametersSnippet = getRequestParameters();
        ResponseFieldsSnippet responseFieldsSnippet = getResponseFields();
        return 문서_필터생성(identifier, requestParametersSnippet, responseFieldsSnippet);
    }

    public static RestDocumentationFilter 문서_필터생성(String identifier, RequestParametersSnippet requestParametersSnippet, ResponseFieldsSnippet responseFieldsSnippet) {
        return document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet
        );
    }


    public static Map<String, String> 경로_조회_파라미터_생성(Long source, Long target, String type) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");
        params.put("type", type);
        return params;
    }




}
