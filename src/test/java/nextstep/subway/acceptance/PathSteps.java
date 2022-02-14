package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;

public class PathSteps {

    public static RequestSpecification 경로_조회하기_문서화_스펙_정의(RestDocumentationContextProvider restDocumentation) {
        return new RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation))
            .addFilter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("source").description("경로조회의 시작역 아이디"),
                    parameterWithName("target").description("경로조회의 도착역 아이디"),
                    parameterWithName("pathType").description("경로조회의 기준 (DISTANCE(최단거리) or DURATION(최소시간))")),
                responseFields(
                    fieldWithPath("stations[].id").type(Long.class).description("역 아이디"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                    fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("역 생성날짜"),
                    fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("역 수정날짜"),
                    fieldWithPath("distance").type(Integer.class).description("경로조회 총 거리"),
                    fieldWithPath("duration").type(Integer.class).description("경로조회 총 소요시간"),
                    fieldWithPath("fare").type(Integer.class).description("경로조회 총 비용")
                )))
            .build();
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", source)
            .queryParam("target", target)
            .queryParam("pathType", PathType.DISTANCE)
            .when().get("/paths")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", source)
            .queryParam("target", target)
            .queryParam("pathType", PathType.DURATION)
            .when().get("/paths")
            .then().log().all().extract();
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation
        , int distance, int duration) {
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

    public static Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId
        , int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    public static void 경로조회의_결과_경로가_예상과_같다(ExtractableResponse<Response> response
    , Long... stationIdList) {
        assertThat(response.jsonPath().getList("stations.id", Long.class))
            .containsExactly(stationIdList);
    }

    public static void 경로조회의_결과_정보가_예상과_같다(ExtractableResponse<Response> response
        , int distance, int duration, int fare) {
        Assertions.assertAll(
            () -> assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration),
            () -> assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance),
            () -> assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare)
        );
    }
}
