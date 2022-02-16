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

    public static ExtractableResponse<Response> 경로조회_및_문서_생성_최단_거리_기준(RequestSpecification spec, RestDocumentationFilter filter, Map<String, String> params) {
        return RestAssured
                .given(spec).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 경로조회_및_문서_생성_최소_시간_기준(RequestSpecification spec, RestDocumentationFilter filter, Map<String, String> params) {
        return RestAssured
                .given(spec).log().all()
                .filter(filter)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths/minimum-time")
                .then().log().all().extract();
    }

    public static Map<String, String> 경로_조회_파라미터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", "2");
        return params;
    }

    public static RestDocumentationFilter 경로관련_문서_필터생성(String identifier, RequestParametersSnippet requestParametersSnippet, ResponseFieldsSnippet responseFieldsSnippet) {
        RestDocumentationFilter filter = document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParametersSnippet,
                responseFieldsSnippet
        );
        return filter;
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 최단금액_거리_경로조회_시간_요금_포함(Long source, Long target) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths/minimum-fee")
                .then().log().all().extract();
        return response;
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/paths/minimum-time")
                .then().log().all().extract();
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

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
                fieldWithPath("duration").description("해당 경로 소요 시간")
        );
        return responseFieldsSnippet;
    }

    public static RequestParametersSnippet getRequestParameters() {
        RequestParametersSnippet requestParametersSnippet = requestParameters(
                parameterWithName("source").description("조회하는 경로에 상행역"),
                parameterWithName("target").description("조회하는 경로에 하행역")
        );
        return requestParametersSnippet;
    }

    public static PathResponse getPathResponse() {
        PathResponse pathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 3
        );
        return pathResponse;
    }

    public static void 경로조회_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1, 2);
        assertThat(response.jsonPath().getList("stations.name")).containsExactly("강남역", "역삼역");
    }


}
