package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(final String accessToken, final Long source, final Long target, final String type) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance, int duration) {
        Map<String, Object> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    public static Long 지하철_노선_생성_요청(final String name, final String color, final Long upStation
            , final Long downStation, final int distance, final int duration, final BigDecimal extraFare) {
        Map<String, Object> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("extraFare", extraFare + "");
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

    public static RequestSpecification given(final RequestSpecification spec) {
        return RestAssured
                .given(spec).log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    public static RequestParametersSnippet getPathRequestParametersSnippet() {
        return requestParameters(
                parameterWithName("source").description("The source is station for ascending"),
                parameterWithName("target").description("The target is station for descending"),
                parameterWithName("type").description("The type is station for search condition"));
    }

    public static ResponseFieldsSnippet getPathResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations").description("The stations is array."),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("The id of the station"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("The name of the station"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("The distance is shortest distance between two stations."),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("The duration is minimum time between two stations."),
                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("The fare is amount for route.")
        );
    }
}
