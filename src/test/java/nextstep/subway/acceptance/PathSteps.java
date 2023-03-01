package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청(RequestSpecification spec, long source, long target, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        params.put("type", type);

        return RestAssured
                .given(spec).log().all()
                .filter(document("path", requestParameters(
                                parameterWithName("source").description("시작 지하철역 id"),
                                parameterWithName("target").description("종료 지하철역 id"),
                                parameterWithName("type").description("최단경로 검색 기준 타입 ("+ PathType.시간.getType() +", "+ PathType.거리.getType() +")")),
                        responseFields(
                                fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("결과코드"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총거리 (km)"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간 (분)"),
                                fieldWithPath("totalFare").type(JsonFieldType.NUMBER).description("이용요금 (원)")
                        )))
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원계정으로_두_역의_경로_조회를_요청(RequestSpecification spec, String accessToken, long source, long target, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        params.put("type", type);

        return RestAssured
                .given(spec).log().all()
                .filter(document("path",  requestHeaders(
                                headerWithName("Authorization").description("Bearer Auth credentials")),
                        requestParameters(
                                parameterWithName("source").description("시작 지하철역 id"),
                                parameterWithName("target").description("종료 지하철역 id"),
                                parameterWithName("type").description("최단경로 검색 기준 타입 ("+ PathType.시간.getType() +", "+ PathType.거리.getType() +")")),
                        responseFields(
                                fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("결과코드"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총거리 (km)"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간 (분)"),
                                fieldWithPath("totalFare").type(JsonFieldType.NUMBER).description("이용요금 (원)")
                        )))
                .header("Authorization", makeBrearerAccessToken(accessToken))
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static Object makeBrearerAccessToken(String accessToken) {
        return "bearer "+accessToken;
    }
}
