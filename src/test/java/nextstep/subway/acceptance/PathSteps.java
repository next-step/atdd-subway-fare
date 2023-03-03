package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    public static RequestSpecification baseDocumentRequest(RequestSpecification spec, String directory, Integer port) {
        return RestAssured
            .given(spec).port(port).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .filter(document("path" + directory,
                getRequestHeadersSnippet(),
                getRequestParametersSnippet(),
                getResponseFieldsSnippet()));
    }

    private static RequestHeadersSnippet getRequestHeadersSnippet() {
        return requestHeaders(
            headerWithName("authorization").description("액세스 토큰").optional()
        );
    }

    private static ResponseFieldsSnippet getResponseFieldsSnippet() {
        return responseFields(
            fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로에 포함되는 역의 목록"),
            fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역의 아이디"),
            fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역의 번호"),
            fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
            fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요 시간"),
            fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금")
        );
    }

    private static RequestParametersSnippet getRequestParametersSnippet() {
        return requestParameters(
            parameterWithName("source").description("출발역"),
            parameterWithName("target").description("도착역"),
            parameterWithName("type").description("조회타입")
        );
    }

    private static RequestSpecification baseRequest() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return baseRequest()
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathType}", source, target, PathType.DISTANCE)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return baseRequest()
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathType}", source, target, PathType.DURATION)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰을_가지고_두_역의_최단_거리_경로_조회를_요청(Long source, Long target, String accessToken) {
        return baseRequest()
            .auth().oauth2(accessToken)
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathType}", source, target, PathType.DISTANCE)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰을_가지고_두_역의_최소_시간_경로_조회를_요청(Long source, Long target, String accessToken) {
        return baseRequest()
            .auth().oauth2(accessToken)
            .when().get("/paths?source={sourceId}&target={targetId}&type={pathType}", source, target, PathType.DURATION)
            .then().log().all().extract();
    }
}
